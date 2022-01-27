import java.io.*;
import java.net.BindException;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Scanner;

public class Parser {
    String[] memory = new String[35];

    String a = " ";
    String b = " ";
    public Parser() {
        memory[18] = "p1a= ";
        memory[19] = "p1b= ";
        memory[25] = "p2a= ";
        memory[26] = "p2b= ";
        memory[33] = "p3a= ";
        memory[34] = "p3b= ";
    }

    public void scheduler() throws IOException {
         a = "";
         b = "";

        boolean exit = false;
        int pNum = 1;
        String[] rowCont = (memory[Integer.valueOf((memory[3].split("="))[1])].split("="))[1].split(" ");
        int index = -1;
        boolean secondRound = false;
        int PCIndex = -1;
        int PC = -1;
        int stateIndex = -1;
        int max = -1;
        int aIndex = -1;
        int bIndex = -1;

        while (exit != true) {
            if (pNum == 1) {
                System.out.println("Scheduler chose program "+pNum);
                if (memory[1].equals("p1State=finished"))
                    pNum++;
                else {
                    aIndex = 18;
                    bIndex = 19;
                    stateIndex = 1;
                    PCIndex = 2;
                    System.out.println("Program 1 state updated to 'Running' in PCB area in memory, index: " + stateIndex);
                    a = memory[aIndex].split("=")[1];
                    b = memory[bIndex].split("=")[1];
                    System.out.println("Variables 'a' and 'b' read from memory");
                    PC = Integer.valueOf((memory[2].split("="))[1]);
                    max = Integer.valueOf((memory[4].split("="))[1]);
                    index = Integer.valueOf((memory[3].split("="))[1]) + (PC);
                    rowCont = (memory[index].split("="))[1].split(" ");
                    memory[stateIndex] = "p1State=running";
                    memory[PCIndex] = "p1PC=" + ((Integer.valueOf((memory[2].split("="))[1])) + 1);
                    System.out.println("PC value read from memory then incremented by 1 in memory index: " + PCIndex);
                }
            }
            if (pNum == 2) {
                System.out.println("Scheduler chose program "+pNum);
                if (memory[5].equals("p2State=finished"))
                    pNum++;
                else {
                    aIndex = 25;
                    bIndex = 26;
                    max = Integer.valueOf((memory[9].split("="))[1]);
                    stateIndex = 6;
                    PCIndex = 7;
                    System.out.println("Program 2 state updated to 'Running' in PCB area in memory, index: " + stateIndex);
                    a = memory[aIndex].split("=")[1];
                    b = memory[bIndex].split("=")[1];
                    System.out.println("Variables 'a' and 'b' read from memory");
                    PC = Integer.valueOf((memory[7].split("="))[1]);
                    index = Integer.valueOf((memory[8].split("="))[1]) + (PC);
                    rowCont = (memory[index].split("="))[1].split(" ");
                    memory[stateIndex] = "p2State=running";
                    memory[PCIndex] = "p2PC=" + ((Integer.valueOf((memory[7].split("="))[1])) + 1);
                    System.out.println("PC value read from memory then incremented by 1 in memory index: " + PCIndex);
                }
            }
            if (pNum == 3) {
                System.out.println("Scheduler chose program "+pNum);
                if (memory[10].equals("p3State=finished"))
                    pNum++;
                else {
                    aIndex = 33;
                    bIndex = 34;
                    max = Integer.valueOf((memory[14].split("="))[1]);
                    PCIndex = 12;
                    stateIndex = 11;
                    System.out.println("Program 3 state updated to 'Running' in PCB area in memory, index: " + stateIndex);
                    a = memory[aIndex].split("=")[1];
                    b = memory[bIndex].split("=")[1];
                    System.out.println("Variables 'a' and 'b' read from memory");
                    PC = Integer.valueOf((memory[12].split("="))[1]);
                    index = Integer.valueOf((memory[13].split("="))[1]) + (PC);
                    rowCont = (memory[index].split("="))[1].split(" ");
                    memory[stateIndex] = "p3State=running";
                    memory[PCIndex] = "p3PC=" + ((Integer.valueOf((memory[12].split("="))[1])) + 1);
                    System.out.println("PC value read from memory then incremented by 1 in memory index: " + PCIndex);
                }
            }

            for (int i = 0; i < rowCont.length; i++) {
                if (rowCont[i].equals("assign")) {
                    assign(rowCont);
                    i = rowCont.length - 1;
                }
                if (rowCont[i].equals("print"))
                    Print(rowCont[++i]);
                if (rowCont[i].equals("writeFile"))
                    WriteFile(rowCont[++i], rowCont[++i]);
                if (rowCont[i].equals("add"))
                    add(rowCont[++i], rowCont[++i]);
                if (!a.equals(" ")) {
                    System.out.println(("Variable 'a' value: " + a + " getting stored in index: " + aIndex + " in memory.").replace("\n", ""));
                    memory[aIndex] = "p" + pNum + "a=" + a;
                }
                if (!b.equals(" ")) {
                    System.out.println(("Variable 'b' value: " + b + " getting stored in index: " + bIndex + " in memory.").replace("\n", ""));
                    memory[bIndex] = "p" + pNum + "b=" + b;
                }
                index++;
                if (index <= max) {
                    if (secondRound == false) {
                        secondRound = true;
                    } else {
                        memory[stateIndex] = (memory[stateIndex].split("=")[0]) + "=notRunning";
                        secondRound = false;
                        pNum++;
                    }
                } else {
                    memory[stateIndex] = (memory[stateIndex].split("=")[0]) + "=finished";
                    System.out.println("scheduler finished executing program "+pNum);
                    String quanta = "";
                    while(PC/2>0){
                        quanta =quanta+ " "+2;
                        PC--;
                        PC--;
                    }
                    if(secondRound == false){
                        System.out.println("program "+pNum+" quanta is "+quanta+" 1");
                    }
                    else{
                        System.out.println("program "+pNum+" quanta is "+quanta+ " 2");
                        secondRound= false ;
                    }
                    pNum++;

                }
                System.out.println("-------------------------------------------------");

            }
            if (pNum == 4)
                pNum = 1;
            if (memory[1].equals("p1State=finished") && memory[6].equals("p2State=finished")
                    && memory[11].equals("p3State=finished"))
                exit = true;
        }
        System.out.println("Final PCB content: " + memory[0] + " " + memory[1] + " " + memory[2] + " " + memory[3] + " " + memory[4] + " " + memory[5]
                + " " + memory[6] + " " + memory[7] + " " + memory[8] + " " + memory[9] + " " + memory[10] + " " + memory[11] + " " + memory[12] + " " + memory[13] + " " + memory[14]
        );
        System.out.println(Arrays.toString(memory));
    }

    public void PCB() {
        memory[0] = "p1ID=1";
        memory[1] = "p1State=notRunning";
        memory[2] = "p1PC=0";
        memory[3] = "p1Min=15";
        memory[4] = "p1Max=17";
        System.out.println("Program 1 PCB initialized.");
        System.out.println("PCB content: " + memory[0] + " " + memory[1] + " " + memory[2] + " " + memory[3] + " " + memory[4]);
        memory[5] = "p2ID=2";
        memory[6] = "p2State=notRunning";
        memory[7] = "p2PC=0";
        memory[8] = "p2Min=20";
        memory[9] = "p2Max=24";
        System.out.println("Program 2 PCB initialized.");
        System.out.println("PCB content: " + memory[5] + " " + memory[6] + " " + memory[7] + " " + memory[8] + " " + memory[9]);
        memory[10] = "p3ID=3";
        memory[11] = "p3State=notRunning";
        memory[12] = "p3PC=0";
        memory[13] = "p3Min=27";
        memory[14] = "p1Max=32";
        System.out.println("Program 3 PCB initialized.");
        System.out.println("PCB content: " + memory[10] + " " + memory[11] + " " + memory[12] + " " + memory[13] + " " + memory[14]);
        System.out.println("-------------------------------------------------");

    }

    public void unparsedInstructionIntoMemory() {
        PCB();
        String[] boundaryReaderMin;
        String[] boundaryReaderMax;
        String nextLine;
        int i;
        int j;
        try {
            int currentFileCounter = 1;
            while (currentFileCounter < 4) {
                File f = new File("Program " + currentFileCounter + ".txt");
                if (f.exists()) {
                    Scanner sc = new Scanner(f);
                    switch (currentFileCounter) {
                        case 1:
                            boundaryReaderMin = memory[3].split("=");
                            boundaryReaderMax = memory[4].split("=");
                            i = Integer.parseInt(boundaryReaderMin[1]);
                            j = 1;
                            while (i <= Integer.parseInt(boundaryReaderMax[1])) {
                                nextLine = sc.nextLine();
                                System.out.println("Program " + currentFileCounter + " Instruction " + j + ": " + nextLine);
                                memory[i] = "p" + currentFileCounter + "Instruction" + "=" + nextLine;
                                i++;
                                j++;
                            }
                            break;
                        case 2:
                            boundaryReaderMin = memory[8].split("=");
                            boundaryReaderMax = memory[9].split("=");
                            i = Integer.parseInt(boundaryReaderMin[1]);
                            j = 1;
                            while (i <= Integer.parseInt(boundaryReaderMax[1])) {
                                nextLine = sc.nextLine();
                                System.out.println("Program " + currentFileCounter + " Instruction " + j + ": " + nextLine);
                                memory[i] = "p" + currentFileCounter + "Instruction" + "=" + nextLine;
                                i++;
                                j++;
                            }
                            break;
                        case 3:
                            boundaryReaderMin = memory[13].split("=");
                            boundaryReaderMax = memory[14].split("=");
                            i = Integer.parseInt(boundaryReaderMin[1]);
                            j = 1;
                            while (i <= Integer.parseInt(boundaryReaderMax[1])) {
                                nextLine = sc.nextLine();
                                System.out.println("Program " + currentFileCounter + " Instruction " + j + ": " + nextLine);
                                memory[i] = "p" + currentFileCounter + "Instruction" + "=" + nextLine;
                                i++;
                                j++;
                            }
                            break;
                    }
                    currentFileCounter++;
                } else
                    throw new Exception("File is missing.");
            }
            scheduler();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String readInput() {
        Scanner sc = new Scanner(System.in);
        // sc.close();
        return sc.nextLine();
    }

    public String ReadFile(String s) {
        String data = "";
        try {
            String file = "";
            if (s.equals("a"))
                file = a;
            else
                file = b;
            File myObj = new File(file);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                data += myReader.nextLine() + "\n";
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return data;
    }

    public void WriteFile(String fileName, String data) throws IOException {
        if (fileName.equals("a"))
            fileName = a;
        else
            fileName = b;
        // String fileN = System.getProperty("user.dir") + fileName + ".txt";
        String fileN = fileName + ".txt";
        File file = new File(fileN);
        file.createNewFile();
        FileWriter fileWriter = new FileWriter(fileN);
        if (data.equals("a"))
            data = a;
        else
            data = b;
        fileWriter.write(data);
        fileWriter.close();
    }

    public void assign(String[] rowCont) {
        for (int i = 0; i < rowCont.length; i++) {
            if (rowCont[i].equals("assign")) {
                i++;
                if (rowCont[i].equals("a")) {
                    i++;
                    if (rowCont[i].equals("readFile"))
                        a = ReadFile(rowCont[++i]);
                    else if (rowCont[i].equals("input"))
                        a = readInput();
                    else
                        a = (rowCont[++i]);
                } else {
                    i++;
                    if (rowCont[i].equals("readFile"))
                        b = ReadFile(rowCont[++i]);
                    else if (rowCont[i].equals("input"))
                        b = readInput();
                    else
                        b = (rowCont[++i]);
                }
            }
        }
    }

    public void Print(String k) {
        if (k.equals("a"))
            System.out.println(a);
        else if (k.equals("b"))
            System.out.println(b);
        else
            System.out.println(k);
    }

    public void add(String x, String y) {
        if (x.equals("a")) {
            if (y.equals("b"))
                a = String.valueOf(Integer.parseInt((String) a) + Integer.parseInt((String) b));
            else if (y.equals("a"))
                a = String.valueOf(Integer.parseInt((String) a) + Integer.parseInt((String) a));
        } else {
            if (y.equals("a"))
                b = String.valueOf(Integer.parseInt((String) b) + Integer.parseInt((String) a));
            else if (y.equals("b"))
                b = String.valueOf(Integer.parseInt((String) b) + Integer.parseInt((String) b));
        }
    }

    public static void main(String[] args) throws IOException {
        Parser p = new Parser();
        p.unparsedInstructionIntoMemory();
    }
}
