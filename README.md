# MediaDemo
write some test code for media with android api

some tips for AudioRecord and AudioTrack using:

1. AudioRecord just like a writer to write PCM data into a file; while AudioTack like a Reader to read PCM data from the file;

2. before initialize AudioRecord and AudioTrack, need to get MinBufferSize first which will be needed to initialize AudioRecord and AudioTrack;

3. to initialize AudioRecord and AudioTrack, some config settings should be given:

     A. samplefrequencerate:(needed Both by AudioRecord and AudioTack)
     B. channelconfig:(both)
     C. encodingformate:(both)
     D. streamtype:(audiotrack only)
     E. mode:(audiotrack only)

4. the other things you need to do is just do it as java I/O;

hope you can handle it well!
