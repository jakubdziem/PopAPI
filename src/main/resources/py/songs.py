import base64
import difflib
import os
import re
import time
from io import BytesIO

import requests
import unicodedata
from PIL import Image

client_id = ''
client_secret = ''

# Directory to save downloaded images
output_directory = 'src/main/resources/static/images/spotify/songs'


# Function to get Spotify API token
def get_spotify_token(client_id, client_secret):
    auth_url = 'https://accounts.spotify.com/api/token'
    auth_header = base64.b64encode(f"{client_id}:{client_secret}".encode()).decode('ascii')
    headers = {
        'Authorization': f'Basic {auth_header}',
    }
    data = {
        'grant_type': 'client_credentials',
    }
    response = requests.post(auth_url, headers=headers, data=data)

    if response.status_code == 200:
        response_data = response.json()
        print("Token successfully retrieved.")
        return response_data['access_token']
    else:
        print(f"Failed to retrieve token: {response.status_code}, {response.text}")
        return None


# Function to remove accents and convert characters to their closest ASCII equivalent
def normalize_string(text):
    normalized = unicodedata.normalize('NFKD', text)
    return ''.join([c for c in normalized if not unicodedata.combining(c)])


# Function to strip everything after "ft." from the artist name
def clean_artist_name(artist_name):
    return re.sub(r'\s*ft\..*$', '', artist_name, flags=re.IGNORECASE).strip()


# Function to search for a song by name and artist (fallback to only song name if necessary)
def search_song(song_name, artist_name, token):
    print(f"Starting search for '{song_name}' by '{artist_name}'...")

    cleaned_artist_name = clean_artist_name(artist_name)
    print(f"Cleaned artist name: {cleaned_artist_name}")

    # Attempt to search by both song name and artist
    search_result = search_by_song_and_artist(song_name, cleaned_artist_name, token)

    if search_result:
        print(f"Found result for '{song_name}' by '{artist_name}'")
        return search_result
    else:
        print(f"Couldn't find '{song_name}' by '{artist_name}'. Trying by song name only...")
        return search_by_song_only(song_name, token)


# Function to search by both song name and artist


def search_by_song_and_artist(song_name, artist_name, token):
    search_url = 'https://api.spotify.com/v1/search'
    headers = {
        'Authorization': f'Bearer {token}',
    }
    params = {
        'q': f'track:"{song_name}" artist:"{artist_name}"',
        'type': 'track',
        'limit': 5
    }

    retry_count = 0
    max_retries = 5  # Limit the number of retries

    while retry_count < max_retries:
        response = requests.get(search_url, headers=headers, params=params)

        if response.status_code == 200:
            response_data = response.json()
            if response_data['tracks']['items']:
                tracks = response_data['tracks']['items']

                best_match = None
                highest_score = 0

                for track in tracks:
                    normalized_api_song_name = normalize_string(track['name'].lower())
                    song_similarity_ratio = difflib.SequenceMatcher(None, song_name.lower(),
                                                                    normalized_api_song_name).ratio()

                    popularity_score = track.get('popularity', 0)
                    if popularity_score >= 10:  # Filter out low popularity songs
                        combined_score = (song_similarity_ratio * 0.6) + (popularity_score / 100 * 0.4)

                        if combined_score > highest_score:
                            highest_score = combined_score
                            best_match = track

                if best_match:
                    track_id = best_match['id']
                    song_name = best_match['name']
                    track_image_url = best_match['album']['images'][0]['url'] if best_match['album'].get(
                        'images') else None
                    print(f"Found best match: {song_name} (Combined Score: {highest_score:.2f})")
                    return song_name, track_id, track_image_url
                else:
                    print(f"No suitable match found for: {song_name}")
                    return None
            else:
                print(f"No match found for: {song_name}")
                return None

        elif response.status_code == 429:
            retry_after = int(response.headers.get('Retry-After', 60))
            retry_count += 1
            print(f"Rate limit reached. Retrying after {retry_after} seconds... (Retry {retry_count}/{max_retries})")
            time.sleep(retry_after)
        else:
            print(f"Failed to search for song: {song_name}, Status Code: {response.status_code}, {response.text}")
            return None

    print(f"Max retries reached for: {song_name} by {artist_name}. Skipping.")
    return None


# Function to search by song name only
def search_by_song_only(song_name, token):
    search_url = 'https://api.spotify.com/v1/search'
    headers = {
        'Authorization': f'Bearer {token}',
    }
    params = {
        'q': f'track:"{song_name}"',  # Search only by track name
        'type': 'track',
        'limit': 5
    }

    # Normalize the search song name
    normalized_song_name = normalize_string(song_name.lower())

    while True:
        response = requests.get(search_url, headers=headers, params=params)
        if response.status_code == 200:
            response_data = response.json()
            if response_data['tracks']['items']:
                tracks = response_data['tracks']['items']

                best_match = None
                highest_score = 0

                for track in tracks:
                    normalized_api_song_name = normalize_string(track['name'].lower())
                    song_similarity_ratio = difflib.SequenceMatcher(None, normalized_song_name,
                                                                    normalized_api_song_name).ratio()

                    popularity_score = track.get('popularity', 0)

                    if popularity_score >= 10:  # Filter out low popularity songs
                        combined_score = (song_similarity_ratio * 0.6) + (popularity_score / 100 * 0.4)

                        if combined_score > highest_score:
                            highest_score = combined_score
                            best_match = track

                if best_match:
                    track_id = best_match['id']
                    song_name = best_match['name']
                    track_image_url = best_match['album']['images'][0]['url'] if best_match['album'].get(
                        'images') else None
                    print(f"Found best match: {song_name} (Combined Score: {highest_score:.2f})")
                    return song_name, track_id, track_image_url
            else:
                print(f"No match found for: {song_name}")
                return None
        elif response.status_code == 429:
            retry_after = int(response.headers.get('Retry-After', 60))
            print(f"Rate limit reached. Retrying after {retry_after} seconds...")
            time.sleep(retry_after)
        else:
            print(f"Failed to search for song: {song_name}, Status Code: {response.status_code}, {response.text}")
            return None


# Function to download and save song's image
def download_track_image(image_url, file_path):
    while True:
        response = requests.get(image_url)

        if response.status_code == 200:
            img = Image.open(BytesIO(response.content))
            img.save(file_path)  # Save the image to a file
            return True
        elif response.status_code == 429:
            retry_after = int(response.headers.get('Retry-After', 60))
            print(f"Rate limit reached while downloading image. Retrying after {retry_after} seconds...")
            time.sleep(retry_after)
        else:
            print(f"Failed to retrieve image: {image_url}, Status Code: {response.status_code}")
            return False


# Main function
def main():
    print("Starting the main function...")
    token = get_spotify_token(client_id, client_secret)

    if not token:
        print("Failed to retrieve Spotify API token. Exiting.")
        return

    print("Token retrieved, reading file...")

    try:
        with open('src/main/resources/updatingImages/spotifySongsInFormatSuitableToExtractImages.txt', 'r', encoding='utf-8') as file:
            song_artist_pairs = [line.strip().split(';') for line in file.readlines() if line.strip()]
    except FileNotFoundError:
        print(
            "The file 'spotifySongsInFormatSuitableToExtractImages.txt' was not found. Please make sure the file exists.")
        return

    print(f"File read successfully, found {len(song_artist_pairs)} songs to process.")

    for song_name, artist_name in song_artist_pairs:
        print(f"Processing song: {song_name} by {artist_name}...")
        result = search_song(song_name, artist_name, token)

        if result:
            name, track_id, track_image_url = result
            print(f"Song Name: {name} by {artist_name}")

            if track_image_url:
                print(f"Song Image URL: {track_image_url}")
                sanitized_file_name = f"{song_name} - {artist_name}".replace('/', ' ').replace('?', ' ').replace('*',
                                                                                                                 ' ').replace(
                    ':', ' ').replace('\"', ' ').replace('\\', ' ').replace('<', ' ').replace('>', ' ').replace('|',
                                                                                                                ' ') + ".jpg"
                file_path = os.path.join(output_directory, sanitized_file_name)
                success = download_track_image(track_image_url, file_path)
                if success:
                    print(f"Image saved to: {file_path}")
                else:
                    print(f"Failed to download image for {name}")
            else:
                print(f"No image found for song: {name}")
        else:
            print(f"Skipping song: {song_name} by {artist_name} due to search failure.")


if __name__ == "__main__":
    main()
