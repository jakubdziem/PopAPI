import time  # Import the time module
import requests
from PIL import Image
from io import BytesIO
import base64
import os
import difflib
import unicodedata

# Spotify API credentials (replace with your actual credentials)
client_id = ''
client_secret = ''

# Directory to save downloaded images
output_directory = 'src/main/resources/static/images/spotify'


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


# Function to search for an artist by name and return the best match considering similarity and popularity
def search_artist(artist_name, token):
    # Check for Tiësto specifically and use his known Spotify ID
    if artist_name == "TiĂ«sto":
        artist_name = "tiesto"

    search_url = 'https://api.spotify.com/v1/search'
    headers = {
        'Authorization': f'Bearer {token}',
    }
    params = {
        'q': artist_name,  # General search query
        'type': 'artist',
        'limit': 5  # Get multiple results to choose the closest match
    }

    # Normalize the search artist name to remove special characters
    normalized_artist_name = normalize_string(artist_name.lower())

    while True:  # Retry loop
        response = requests.get(search_url, headers=headers, params=params)
        if response.status_code == 200:
            response_data = response.json()
            if response_data['artists']['items']:
                artists = response_data['artists']['items']

                # Initialize variables to keep track of the best match
                best_match = None
                highest_score = 0

                # Compare each artist name to the normalized search name
                for artist in artists:
                    # Normalize the artist name from the API response
                    normalized_api_artist_name = normalize_string(artist['name'].lower())

                    # Calculate the similarity ratio
                    similarity_ratio = difflib.SequenceMatcher(None, normalized_artist_name,
                                                               normalized_api_artist_name).ratio()

                    # Get the artist's popularity (between 0 and 100)
                    popularity_score = artist.get('popularity', 0)

                    # Only consider artists with a reasonable popularity
                    if popularity_score >= 50:  # Filter out low popularity artists
                        # Combine similarity and popularity into a weighted score
                        combined_score = (similarity_ratio * 0.5) + (popularity_score / 100 * 0.5)

                        # If this artist has a better combined score, update the best match
                        if combined_score > highest_score:
                            highest_score = combined_score
                            best_match = artist

                # Return the artist with the best combined score
                if best_match:
                    artist_id = best_match['id']
                    artist_name = best_match['name']
                    artist_image_url = best_match['images'][0]['url'] if best_match['images'] else None
                    print(f"Found best match: {artist_name} (Combined Score: {highest_score:.2f})")
                    return artist_name, artist_id, artist_image_url
                else:
                    print("No suitable artist found with sufficient popularity.")
                    return None
            else:
                print(f"No artist found for: {artist_name}")
                return None
        elif response.status_code == 429:
            retry_after = int(
                response.headers.get('Retry-After', 60))  # Get retry time from headers or default to 60 seconds
            print(f"Rate limit reached. Retrying after {retry_after} seconds...")
            time.sleep(retry_after)  # Wait before retrying
        else:
            print(f"Failed to search for artist: {artist_name}, Status Code: {response.status_code}, {response.text}")
            return None


# Function to download and save artist's image to a file
def download_artist_image(image_url, file_path):
    while True:  # Retry loop
        response = requests.get(image_url)

        if response.status_code == 200:
            img = Image.open(BytesIO(response.content))
            img.save(file_path)  # Save the image to a file
            return True
        elif response.status_code == 429:
            retry_after = int(
                response.headers.get('Retry-After', 60))  # Get retry time from headers or default to 60 seconds
            print(f"Rate limit reached while downloading image. Retrying after {retry_after} seconds...")
            time.sleep(retry_after)  # Wait before retrying
        else:
            print(f"Failed to retrieve image: {image_url}, Status Code: {response.status_code}")
            return False


# Main function
def main():
    token = get_spotify_token(client_id, client_secret)

    if not token:
        print("Failed to retrieve Spotify API token. Exiting.")
        return

    # Read artist names from the file
    try:
        with open('src/main/resources/updatingImages/spotifyArtistsInFormatSuitableToExtractImages.txt', 'r', encoding='utf-8') as file:
            artist_names = [line.strip() for line in file.readlines() if line.strip()]
    except FileNotFoundError:
        print("The file 'spotifyArtistsInFormatSuitableToExtractImages.txt' was not found. Please make sure the file "
              "exists.")
        return

    for artist_name in artist_names:
        result = search_artist(artist_name, token)

        if result:
            name, artist_id, artist_image_url = result
            print(f"Artist Name: {name}")

            if artist_image_url:
                print(f"Artist Image URL: {artist_image_url}")
                file_path = os.path.join(output_directory, f"{artist_name.replace('/', ' ')}.jpg")
                success = download_artist_image(artist_image_url, file_path)
                if success:
                    print(f"Image saved to: {file_path}")
                else:
                    print(f"Failed to download image for {name}")
            else:
                print(f"No image found for artist: {name}")
        else:
            print(f"Skipping artist: {artist_name} due to search failure.")


if __name__ == "__main__":
    main()
