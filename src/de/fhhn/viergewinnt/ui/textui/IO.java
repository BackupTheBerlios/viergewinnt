package de.fhhn.viergewinnt.ui.textui;

import java.io.*;

public class IO {
   private static BufferedReader br =
      new BufferedReader(new InputStreamReader(System.in));

   public static void write(String s) {
      writeAndFlush(s);
   }

   public static String promptAndRead(String s) throws Exception {
      writeAndFlush(s);
      return br.readLine();
   }

   public static int readInt(String s) throws Exception {
      return Integer.parseInt(promptAndRead(s).trim());
   }

   private static void writeAndFlush(String s) {
      System.out.print(s);
      System.out.flush();
   }
}