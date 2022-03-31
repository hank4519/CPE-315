import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

public class lab4 {
    private static LinkedHashMap<String, Integer> address = new LinkedHashMap<>();
    private static ArrayList<String> list = new ArrayList<>();
    private static int memory [] = new int [8192];
    private static String header = "pc   if/id   id/exe   exe/mem   mem/wb";
    private static int [] spaces = new int []{8, 9, 10, 0};
    private static String load_reg = "";
    private static String last_vals = "";
    private static double CPI = 0 ;
    private static int cycles = 0;
    private static boolean piperun = false;
    private static String last_beq = "";
    private static String last_bne = "";
    private static LinkedList <Boolean> beq_vals = new LinkedList<>();
    private static LinkedList <Boolean> bne_vals = new LinkedList<>();
    private static String jump_vals = "";
    private static int ins_count=0;

    private static LinkedHashMap <String, Integer> regs = new LinkedHashMap<String, Integer>(){{
        put("pc", 0);
        put("$0", 0);
        put("$v0",0);
        put("$v1", 0);
        put("$a0", 0);
        put("$a1", 0);
        put("$a2", 0);
        put("$a3", 0);
        put("$t0", 0);
        put("$t1", 0);
        put("$t2", 0);
        put("$t3", 0);
        put("$t4", 0);
        put("$t5", 0);
        put("$t6", 0);
        put("$t7", 0);
        put("$s0", 0);
        put("$s1", 0);
        put("$s2", 0);
        put("$s3", 0);
        put("$s4", 0);
        put("$s5", 0);
        put("$s6", 0);
        put("$s7", 0);
        put("$t8", 0);
        put("$t9", 0);
        put("$sp", 0);
        put("$ra", 0);
    }};

    private static int pc = 0;
    private static String [] pipeline = new String[] {"empty", "empty", "empty", "empty"};
    private static String append = "1";
    private static String stall = "stall";
    private static String v1, v2, v3;

    public static void main(String []args) throws IOException{
        File infile = null;
        File script = null;
        Scanner sb;
        boolean flag = false;
        
        if( 0 < args.length){
            infile = new File (args[0]);
        }
        else{
            System.err.println("No file argument: " + args);
            System.exit(0);
        }
        if (1 < args.length){
            script = new File(args[1]);
            sb = new Scanner(script);
        }
        else{
            sb = new Scanner (System.in);
            flag = true;
        }
        

        
        BufferedReader br = new BufferedReader(new FileReader(infile));
        //Scanner sb = new Scanner(script);
        String str;
        int index = 0;
        while ((str= br.readLine()) != null) {
            if(valid(str)){
                str = str.trim();
                str = str.replace('\t', ' ');
                if(str.indexOf('#') != -1 ){
                    str = remove_comment(str);
                }

                if(str.indexOf(':')!=-1){ //label
                    if(!str.isEmpty()){
                        String before_colon = getBefore(str);
                        String after_colon = getAfter (str);
                        after_colon = after_colon.trim();
                        address.put(before_colon, index);
                        if(!after_colon.equals("")){
                            address.put(after_colon, index);
                            list.add(after_colon);
                        }
                        index += 1;}
                }
                else if(address.containsKey(str)){
                    address.put(str.trim()+ append, index);
                    index += 1 ;
                    list.add(str);
                }
                else if (str.indexOf(':') == -1 && !(str.isEmpty())){
                    address.put(str, index);
                    index += 1;
                    list.add(str);
                }
                else{
                }
            }
        }
        String ins;
        if (flag){
            System.out.print("mips> ");
            while (!(ins = sb.nextLine ()).equals("q")){

                if(ins.equals("h")){
                    show_help();
                }
                else if(ins.equals("d")){
                    print_table();
                }
                else if(ins.equals("s")){
                    System.out.println("        1 instruction(s) executed");
                    executePipeline();
                }
                else if(ins.contains("s")){

                    String [] tmp= ins.split(" ", 2);
                    int num = Integer.parseInt(tmp[1]);
                    System.out.println(  "        " + num + " instruction(s) executed");
                    while (num >0){
                        executePipeline();
                        num--;
                    }
                }
                else if(ins.contains("r")){
                    int last_address = list.size();
                    piperun = true;
                    while (pc != last_address){
                        executePipeline();
                    }
                    System.out.println("\nProgram complete");
                    cycles += 4;  //add remaining instruction cycles
                    clearPipeline();
                    CPI = (double)cycles / ins_count;
                    System.out.printf("CPI = %.3f \t Cycles = %d \t Instructions = %d \n", CPI, cycles, ins_count);
                }
                else if(ins.contains("m")){
                    String [] tmp = ins.split(" ", 3);
                    int start = Integer.parseInt(tmp[1]);
                    int end = Integer.parseInt(tmp[2]);
                    for(int i = start; i <= end ; i++ ){
                        System.out.println("[" + i + "] = " + memory[i]);
                    }
                    System.out.println();
                }
                else if(ins.equals("c")){
                    System.out.println("    Simulator reset\n");
                    clear();
                }
                else{
                    System.out.println("Unidentified ins");
                }
                System.out.print("mips> ");

            }
        }

        while (sb.hasNextLine()){
            ins = sb.nextLine();
            if(ins.equals("q")){
                System.out.println("mips> q");
                break;
            }
            else if(ins.equals("h")){
                System.out.println("mips> h\n");
                show_help();
            }
            else if(ins.equals("d")){
                System.out.println("mips> d\n");
                print_table();
            }
            else if(ins.equals("s")){
                System.out.println("mips> s\n");
                executePipeline();
            }
            else if(ins.contains("s")){
                String [] tmp= ins.split(" ", 2);
                int num = Integer.parseInt(tmp[1]);
                System.out.println("mips> s " + num);
                System.out.println(  "        " + num + " instruction(s) executed");
                while (num >0){
                    executePipeline();
                    num--;
                }
            }
            else if(ins.contains("r")){
                System.out.println("mips> r" );;
                int last_address = list.size();
                piperun = true;
                while (pc != last_address){
                    executePipeline();
                }
                System.out.println("\nProgram complete");
                cycles += 4;  //add remaining instruction cycles
                clearPipeline();
                CPI = (double)cycles / ins_count;
                System.out.printf("CPI = %.3f \t Cycles = %d \t Instructions = %d \n", CPI, cycles, ins_count);
            }
            else if(ins.contains("m")){
                String [] tmp = ins.split(" ", 3);
                int start = Integer.parseInt(tmp[1]);
                int end = Integer.parseInt(tmp[2]);
                System.out.println("mips> m " +start + " " + end);
                for(int i = start; i <= end ; i++ ){
                    System.out.println("[" + i + "] = " + memory[i]);
                }
            }
            else if(ins.equals("c")){
                System.out.println("mips> c ");
                System.out.println("    Simulator reset\n");
                clear();
            }
            else{
                System.out.println("Unidentified ins");
            }
        }

    }
    public static void clearPipeline(){
        for(int i =0; i < pipeline.length; i++){
            pipeline[i] = "";
        }
    }
    public static void movePipeline(String ins){
        pipeline[3] = pipeline[2];
        pipeline[2] = pipeline[1];
        pipeline[1] = pipeline[0];
        pipeline[0] = ins;
    }
    public static boolean load_after_use(){
        String last_ins = pipeline[0];

        //sub, add, slt | addi | lw |
        if(last_ins.equals("sub") || last_ins.equals("add") || last_ins.equals("slt")){
            String[] vs = last_vals.split(",");
            v1 = vs[0];
            v2 = vs[1];
            v3 = vs[2];
            v1 = v1.trim();
            v2 = v2.trim();
            v3 = v3.trim();
            if(v1.equals(load_reg) || v2.equals(load_reg) || v3.equals(load_reg))
                return true;
        }
        else if(last_ins.equals("addi")){
            String[] vs = last_vals.split(",");
            v2 = vs[1]; v2 = v2.trim();
            if(v2.equals(load_reg))
                return true;
        }
        else if (last_ins.equals("lw")){
            String [] vs = last_vals.split(",");
            v1 = vs[0];
            v2 = vs[1];
            v1= v1.trim(); v2= v2.trim();
            String tmp []= v2.split("\\(");
            String rs = tmp [1];
            rs = rs.replace(")", "").trim();
            if(rs.equals(load_reg)){
                return true;
            }
        }
        return false;
    }
    public static void moveStallPipeline(){
        pipeline[3] = pipeline[2];
        pipeline[2] = pipeline[1];
        pipeline[1] = stall;
        //pipeline[0] remains unchanged
    }
    public static void squashPipeline(){
        pipeline[3] = pipeline[2];
        pipeline[2] = pipeline[1];
        pipeline[1] = pipeline[0];
        pipeline[0] = "squash";
    }
    public static void executePipeline() {
        if (pc == regs.get("pc")) {
            execute();
        }
        boolean regular = true;
        String key = list.get(pc);
        String[] arr;
        String instruction = "";
        if (key.contains(" ")) {
            arr = key.split(" ", 2);
        } else {
            arr = key.split("\\$", 2);
        }
        instruction = arr[0]; // instruction like addi
        String vals = arr[1]; // registers
        if(pipeline[0].equals("j") || pipeline[0].equals("jr") || pipeline[0].equals("jal")){
            if(pipeline[0].equals("jr")){
                pc = regs.get(jump_vals.trim());
            }else{
                pc = address.get(jump_vals.trim()+":");
            }
            if(pipeline[0].trim().equals("j")){
                cycles--; //one cycle penalty
            }
            squashPipeline();

            ins_count--;
            regular = false;
        }
        //use after load
        if (instruction.equals("lw")) {
            String[] vs = vals.split(",");
            v1 = vs[0];
            v1 = v1.trim();
            load_reg = v1;
        }else if(instruction.equals("beq")){
            store_beq_val(vals);
            last_beq = vals;
        }else if(instruction.equals("bne")){
            store_bne_val(vals);
            last_bne = vals;
        }
        else if(instruction.equals("j") || instruction.equals("jr") || instruction.equals("jal")){
            jump_vals = vals;
        }
        if (pipeline[1].equals("lw")) {
            if (load_after_use()) {
                moveStallPipeline();
                regular = false;
                ins_count--;
            }
        }
        if(pipeline[2].equals("beq") || pipeline[2].equals("bne")){
            //Check the delay for branches
            if(pipeline[2].equals("beq") && beq_vals.removeFirst()) {
                threeSquash();
                pc = update_beq_pc();
                regular = false;
                ins_count-=3;
                cycles+=1;
            }else if(pipeline[2].equals("bne") && bne_vals.removeFirst()) {
                threeSquash();
                pc = update_bne_pc();
                regular = false;
                ins_count-=3;
                cycles+=1;
            }
        }
        if (regular){
            movePipeline(instruction);
            pc++;
        }
        ins_count++;
        cycles++;
        last_vals = vals;
        if(piperun){
            return;
        }
        System.out.println(header);
        System.out.print(pc + "\t ");
        for(int i = 0; i < pipeline.length; i++){
            System.out.print(pipeline[i]);
            int space = spaces[i] - pipeline[i].length();
            for(int j = 0; j < space; j++){
                System.out.print(" ");
            }
        }
        System.out.println("\n");

    }
    public static int update_beq_pc(){
        String [] vs = last_beq.split(",");
        v3 = vs[2];
        return address.get(v3.trim()+":");
    }
    public static int update_bne_pc(){
        String [] vs = last_bne.split(",");
        v3 = vs[2];
        return address.get(v3.trim()+":");
    }
    public static void store_beq_val(String vals){
        String [] vs = vals.split(",");
        v1 = vs[0];
        v2 = vs[1];
        v1 = v1.trim();v2 = v2.trim();
        if(regs.get(v1).equals(regs.get(v2)))
            beq_vals.add(true);
        else
            beq_vals.add(false);
    }
    public static void store_bne_val(String vals){
        String[] vs = vals.split(",");
        v1 = vs[0];
        v2 = vs[1];
        v1 = v1.trim();v2 = v2.trim();
        if (!regs.get(v1).equals(regs.get(v2)))
            bne_vals.add(true);
        else
            bne_vals.add(false);
    }
    public static void threeSquash() {
        pipeline[3]= pipeline[2];
        pipeline[2] = "squash";
        pipeline[1] = "squash";
        pipeline[0] = "squash";
    }
    public static int bne (String vals, int pc){
        String[] vs = vals.split(",");
        int branch;
        v1 = vs[0];
        v2 = vs[1];
        v3 = vs[2];
        v1 = v1.trim();v2 = v2.trim();v3 = v3.trim();
        if (!regs.get(v1).equals(regs.get(v2))){
            branch = address.get(v3+":");
        }else{
            branch = pc +1;
        }
        return branch;
    }
    public static int beq (String vals, int pc){
        String [] vs = vals.split(",");
        int branch;
        v1 = vs[0];
        v2 = vs[1];
        v3 = vs[2];
        v1 = v1.trim();v2 = v2.trim();v3 = v3.trim();
        if(regs.get(v1).equals(regs.get(v2))){
            branch = address.get(v3+":");
        }else{
            branch = pc+1;
        }
        return branch;

    }

    public static void execute(){
        int pc = regs.get("pc");
        String key = list.get(pc);
        String[] arr;
        String instruction ="";
        if(key.indexOf(':') == -1) {
            if (key.contains(" ")) {
                arr = key.split(" ", 2);
            } else {
                arr = key.split("\\$", 2);
            }
            instruction = arr[0]; // instruction like addi
            String vals = arr[1]; // registers
            if (instruction.equals("addi")){
                addi(vals);
                regs.put("pc", regs.get("pc") + 1);
            }
            else if (instruction.equals("add")){
                add(vals);
                regs.put("pc", regs.get("pc") + 1);
            }
            else if(instruction.equals("sub")){
                sub(vals);
                regs.put("pc", regs.get("pc") + 1);
            }
            else if(instruction.equals("slt")){
                slt(vals);
                regs.put("pc", regs.get("pc") + 1 );
            }
            else if(instruction.equals("bne")){
                int pc_update = bne(vals, regs.get("pc"));
                regs.put("pc", pc_update);
            }
            else if(instruction.equals("beq")){
                //System.out.println("VALS ADAD:" + vals);
                int pc_update = beq(vals, regs.get("pc"));
                regs.put("pc", pc_update);
            }
            else if(instruction.equals("and")){
                and(vals);
                regs.put("pc", regs.get("pc") + 1);
            }
            else if(instruction.equals("or")){
                or(vals);
                regs.put("pc", regs.get("pc") + 1);
            }
            else if(instruction.equals("sw")){
                sw(vals);
                regs.put("pc", regs.get("pc") + 1);
            }
            else if(instruction.equals("jal")){
                int pc_update = jal(vals);
                regs.put("pc", pc_update);
            }
            else if(instruction.equals("jr")){
                int pc_update = jr (vals);
                regs.put("pc", pc_update );
            }
            else if(instruction.equals("j")){
                //System.out.println("VALS : " + vals);
                int pc_update = address.get(vals.trim() + ":");
                regs.put("pc", pc_update);
            }
            else if(instruction.equals("lw")){
                lw(vals);
                regs.put("pc", regs.get("pc") + 1 );
            }
            else{
                System.out.println("Unidentified instruction");
            }
        }
    }


    public static void print_table (){
        System.out.println("pc = " + regs.get("pc"));
        int next_line = 0;
        for(Map.Entry<String, Integer> entry: regs.entrySet()){
            if (entry.getKey().equals("pc")) continue;
            if(next_line== 4){
                System.out.println();
                next_line = 0;
            }
            System.out.print(entry.getKey() + " = " + entry.getValue() + "         ");
            if(entry.getKey().equals("$0")) System.out.print(" ");
            next_line++;
        }
        System.out.println("\n");
    }

    public static void clear(){
        for(Map.Entry<String, Integer> entry : regs.entrySet()){
            regs.put(entry.getKey(), 0);
        }
        Arrays.fill(memory, 0);
    }
    public static void show_help(){
        System.out.println("h = show help");
        System.out.println("d = dump register state");
        System.out.println("s = single step through the program (i.e. execute 1 instruction and stop)");
        System.out.println("s num = step through num instructions of the program");
        System.out.println("r = run until the program ends");
        System.out.println("m num1 num2 = display data memory from location num1 to num2");
        System.out.println("c = clear all registers, memory, and the program counter to 0");
        System.out.println("q = exit the program");
    }

    public static void sub (String vals){
        String[] vs = vals.split(",");
        v1 = vs[0];
        v2 = vs[1];
        v3 = vs[2];
        v1 = v1.trim();
        v2 = v2.trim();
        v3 = v3.trim();
        regs.put(v1, regs.get(v2) - regs.get(v3));
    }



    public static int jr (String vals){
        vals = vals.trim();
        if(vals.length() > 3 ){
            vals = vals.substring(0,3);
        }
        int add = regs.get(vals);
        return add;
    }
    public static int jal(String vals){
        vals = vals.trim();
        if(!address.containsKey(vals+":")){
            vals = vals.substring(0, vals.length() -1);
        }
        int off = address.get(vals +":");
        regs.put("$ra", regs.get("pc") + 1);
        return off;
    }
    public static void lw(String vals){
        String [] vs = vals.split(",");
        v1 = vs[0];
        v2 = vs[1];
        v1= v1.trim(); v2= v2.trim();
        String tmp []= v2.split("\\(");
        String offset = tmp[0];
        String rs = tmp [1];
        rs = rs.replace(")", "").trim();
        int read = memory[regs.get(rs) + Integer.parseInt(offset)];
        regs.put(v1, read);
    }
    public static void sw(String vals){
        String [] vs = vals.split(",");
        v1 = vs[0];
        v2 = vs[1];
        v1= v1.trim(); v2= v2.trim();
        String tmp []= v2.split("\\(");
        String offset = tmp[0];
        String rs = tmp [1];
        rs = rs.replace(")", "").trim(); //rs =
        int meme = regs.get(rs) + Integer.parseInt(offset);
        memory[meme] = regs.get(v1);
    }
    public static void or(String vals){
        String[] vs = vals.split(",");
        v1 = vs[0];
        v2 = vs[1];
        v3 = vs[2];
        v1 = v1.trim();v2 = v2.trim();v3 = v3.trim();
        Integer tmp = regs.get(v2) | regs.get(v3);
        regs.put(v1, tmp);
    }
    public static void and(String vals){
        String[] vs = vals.split(",");
        v1 = vs[0];
        v2 = vs[1];
        v3 = vs[2];
        v1 = v1.trim();v2 = v2.trim();v3 = v3.trim();
        Integer tmp = regs.get(v2) & regs.get(v3);
        regs.put(v1, tmp);
    }

    public static void slt(String vals){
        String[] vs = vals.split(",");
        v1 = vs[0];
        v2 = vs[1];
        v3 = vs[2];
        v1 = v1.trim();
        v2 = v2.trim();
        v3 = v3.trim();
        if (regs.get(v2) < regs.get(v3)) {
            regs.put(v1, 1);
        }else{
            regs.put(v1, 0);
        }
    }
    public static void add(String vals){
        String[] vs = vals.split(",");
        v1 = vs[0];
        v2 = vs[1];
        v3 = vs[2];
        v1 = v1.trim();
        v2 = v2.trim();
        v3 = v3.trim();
        regs.replace(v1, regs.get(v2) + regs.get(v3));
    }
    public static void addi(String vals){
        String[] vs = vals.split(",");
        v1 = vs[0];
        v2 = vs[1];
        v3 = vs[2];
        v1 = v1.trim();
        v2 = v2.trim();
        v3 = v3.trim();
        regs.replace(v1, regs.get(v2) + Integer.parseInt(v3));
    }
    public static String remove_comment(String str){
        StringBuilder sb = new StringBuilder();
        for(char ch: str.toCharArray()){
            if(ch=='#')break;
            sb.append(ch);
        }
        return sb.toString();
    }
    public static String getAfter(String str){
        StringBuilder af = new StringBuilder();
        boolean flag = false;
        for(char ch: str.toCharArray()){
            if(flag) {
                af.append(ch);
                continue;
            }
            if(ch == ':')flag= true;
        }
        return af.toString();
    }
    public static String getBefore(String str){
        StringBuilder bef = new StringBuilder();
        for(char ch: str.toCharArray()){
            if(ch != ':') bef.append(ch) ;
            else break ;
        }
        bef.append(":");
        return bef.toString();
    }
    public static boolean valid(String str) {
        if(str.isEmpty()){
            return false;
        }
        for(char ch: str.toCharArray()){
            if(ch == ' ') continue;
            return ch != '#';
        }
        return false;
    }
}

