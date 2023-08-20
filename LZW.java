/*************************************************************************
 *  Compilation:  javac LZWmod.java
 *  Execution:    java LZWmod - < input.txt > output.lzw  (compress input.txt
 *                                                         into output.lzw)
 *  Execution:    java LZWmod + < output.lzw > input.rec  (expand output.lzw
 *                                                         into input.rec)
 *  Dependencies: BinaryStdIn.java BinaryStdOut.java
 *
 *  Compress or expand binary input from standard input using LZW.
 *
 *
 *************************************************************************/
public class LZW {
    private static final int R = 256;        // alphabet size
    private static boolean flushIfFull = false;

    public static void compress() {
        CompressionCodeBookInterface codebook =
            new DLBCodeBook(9, 16);

        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            if(!codebook.advance(c)){ //found longest match
                int codeword = codebook.getCodeWord();
                BinaryStdOut.write(codeword, codebook.getCodewordWidth());
                codebook.add(flushIfFull);
                codebook.advance(c);
            }
        }
        int codeword = codebook.getCodeWord();
        BinaryStdOut.write(codeword, codebook.getCodewordWidth());

        BinaryStdOut.write(R, codebook.getCodewordWidth());
        BinaryStdOut.close();
    }


    public static void expand() {
      //read first bit of file to see what flushIfFull is
      // and set flushIfFull to that bit
        ExpansionCodeBookInterface codebook = new ArrayCodeBook(9, 16);
        flushIfFull = BinaryStdIn.readBoolean();

        int codeword = BinaryStdIn.readInt(codebook.getCodewordWidth(flushIfFull));
        String val = codebook.getString(codeword);

        while (true) {
            BinaryStdOut.write(val);
            codeword = BinaryStdIn.readInt(codebook.getCodewordWidth(flushIfFull));

            if (codeword == R) break;
            String s = codebook.getString(codeword);
            if (codebook.size() == codeword) s = val + val.charAt(0); // special case hack

            codebook.add(val + s.charAt(0), flushIfFull);
            val = s;

        }
        BinaryStdOut.close();
    }



    public static void main(String[] args) {
      //System.out.println("in main");
        if (args[0].equals("-")) {
          if(args[1].equals("n")){
            flushIfFull = false;
            BinaryStdOut.write(0,1);
          }
          else if(args[1].equals("r")){
            flushIfFull = true;
            BinaryStdOut.write(1,1);
          }
          compress();
        }
        //if arg = -, read new arg if arg is n set flushIfFull to false
        //if arg is r set flushIfFull to true.
        //after setting flushIfFull write bit at beginning of compressed file
        //based on value of flushIfFull
        //use STDin.write(flushIfFull)

        else if (args[0].equals("+")) expand();
        else throw new RuntimeException("Illegal command line argument");
    }

}
