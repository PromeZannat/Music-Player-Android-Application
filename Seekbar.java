SeekBar mSeekBar;
    Handler mHandler;
    Runnable mRunnable;

mSeekBar = findViewById(R.id.seekbar);

mHandler = new Handler();
        initializeSeekBar();

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(myMediaPlayer!=null && b){
                    myMediaPlayer.seekTo(i*1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

public void initializeSeekBar(){
        mSeekBar.setMax(myMediaPlayer.getDuration()/1000);

        mRunnable = new Runnable() {
            @Override
            public void run() {
                if(myMediaPlayer!=null){
                    int mCurrentPosition = myMediaPlayer.getCurrentPosition()/1000; // In milliseconds
                    mSeekBar.setProgress(mCurrentPosition);
                }
                mHandler.postDelayed(mRunnable,1000);
            }
        };
        mHandler.postDelayed(mRunnable,1000);
    }

