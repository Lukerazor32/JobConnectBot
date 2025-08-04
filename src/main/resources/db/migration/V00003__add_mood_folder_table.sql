DROP TABLE IF EXISTS relationship_song_folder;
DROP TABLE IF EXISTS mood_folder;

-- Create mood_folder table
CREATE TABLE mood_folder (
    id SERIAL PRIMARY KEY,
    folder_name VARCHAR(20) UNIQUE);