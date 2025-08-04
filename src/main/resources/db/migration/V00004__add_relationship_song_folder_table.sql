DROP TABLE IF EXISTS relationship_song_folder;

-- Create relationship_song_folder table
CREATE TABLE relationship_song_folder (
    song_id INTEGER NOT NULL,
    mood_folder_id INTEGER NOT NULL,
    FOREIGN KEY (song_id) REFERENCES tg_song(download_id),
    FOREIGN KEY (mood_folder_id) REFERENCES mood_folder(id),
    UNIQUE(song_id, mood_folder_id));