 private void playMusic(music song) {
        try {
            mMediaPlayer.reset();

            mMediaPlayer.setDataSource(song.getFileUrl());
            mMediaPlayer.prepare();

            songIndex = mSongList.indexOf(song);


            AppCompatActivity activity = (AppCompatActivity) getActivity();
            activity.getSupportActionBar().setTitle(song.getTitle());
            activity.getSupportActionBar().setSubtitle(song.getSinger() + " - " + song.getAlbum());


            mMediaPlayer.start();

            mPlayOrPauseImageView.setImageResource(R.drawable.ic_pause);


            mSeekBar.setMax(song.getDuration());


            new Thread(mRunnable).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
