# Import the main functions from artists.py and songs.py
from artists import main as artists_main
from songs import main as songs_main


def run_artists():
    print("Running Artists Module:")
    artists_main()


def run_songs():
    print("Running Songs Module:")
    songs_main()


if __name__ == "__main__":
    print("A")
    # run_artists()
    # run_songs()
