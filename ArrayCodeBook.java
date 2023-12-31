/**
 * An implementation of ExpansionCodeBookInterface using an array.
 */

public class ArrayCodeBook implements ExpansionCodeBookInterface {
    private static final int R = 256;        // alphabet size
    private String[] codebook;
    private int W;       // current codeword width
    private int minW;    // minimum codeword width
    private int maxW;    // maximum codeword width
    private int L;       // maximum number of codewords with
                         // current codeword width (L = 2^W)
    private int code;    // next available codeword value

    public ArrayCodeBook(int minW, int maxW){
        this.maxW = maxW;
        this.minW = minW;
        initialize();
    }
    public int size(){
        return code;
    }


    public int getCodewordWidth(boolean flushIfFull){
      //////////////////////
      if(code >= L && W < maxW){ //if codebook is full && w < maxW
        return W+1;
      } else if (code >= L && W == maxW){//if codebook is full && w = 16 -> check flushIfFull
        //flushIfFull = true -> return minW
        //else -> return 16
          if(flushIfFull = true){
            return minW;
          } else {
            return maxW;
          }
      } else {
        return W;
      }
      //////////////////////
    }

    private void initialize(){
        codebook = new String[1 << maxW];
        W = minW;
        L = 1<<W;
        code = 0;
        // initialize symbol table with all 1-character strings
        for (int i = 0; i < R; i++)
            add("" + (char) i, false);
        add("", false); //R is codeword for EOF
    }

    public void add(String str, boolean flushIfFull) {
        boolean haveRoom = false;
        if(code < L){
            haveRoom = true;
        }   else if(code >= L){
        if(W < maxW){
          W++;
          L = L*2;
          haveRoom = true;
        } else{ //codebook is full && w = 16
          if(flushIfFull = true){
            initialize();
            haveRoom = true;
          } else {//flushIfFull = false
            haveRoom = false;
          }
        }
      }
        //if statement below should be last
        if(haveRoom){
            codebook[code] = str;
            code++;
        }
    }

    public String getString(int codeword) {
        return codebook[codeword];
    }

}
