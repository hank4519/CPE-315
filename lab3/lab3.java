import java.io.*;
import java.util.*;
import java.util.stream.StreamSupport;

public class lab3{
    private static LinkedHashMap<String, Integer> address = new LinkedHashMap<>();
    private static ArrayList<String> list = new ArrayList<>();
    private static int memory [] = new int [8192];
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
    private static int append = 1;
    private static int append_offset = 1;
    public static void main(String[] args) throws IOException {
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
	
        //infile = new File("assembly_file.asm");
        //script = new File ("script_file");
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
                    }
                    index += 1;}
                }
                else if(address.containsKey(str)){
			address.put(str.trim()+ append, index); 
			index += 1 ; 
		}
		else if (str.indexOf(':') == -1 && !(str.isEmpty())){
			address.put(str, index);
			index += 1; 
		}
		else{
		}		
            }
        }
        for (Map.Entry<String, Integer> entry: address.entrySet()) {
            if(!entry.getKey().contains(":"))
            list.add(entry.getKey());
        }
        int j = 0;
        //for(String tmp: list){
          //  System.out.println(tmp + "   " + j++ );
        //}
        //System.out.println(list.size()+"\n\n");
        //System.out.println(list.size() == address.size());
        String ins;
        //for(Map.Entry<String, Integer> entry: address.entrySet()){
            //System.out.println(entry.getKey() + " at " + entry.getValue());
        //}
        //System.out.println(address.size());
	//if (flag){
	//	System.out.print("mips> "); 
	//	ins  = sb.nextLine(); 
	//}
	if (flag){
		System.out.print("mips> "); 
		while (!(ins = sb.nextLine ()).equals( "q")){
			
           		 if(ins.equals("h")){
                	//System.out.println("mips> h\n");
                	show_help();
            		}
            		else if(ins.equals("d")){
               		// System.out.println("mips> d\n");
                		print_table();
           		 }	
            		else if(ins.equals("s")){
                	//System.out.println("mips> s");
                	System.out.println("        1 instruction(s) executed");
               		 execute();
            		}	
            		else if(ins.contains("s")){

                	String [] tmp= ins.split(" ", 2);
                	int num = Integer.parseInt(tmp[1]);
                	//System.out.println("mips> s " + num);
                	System.out.println(  "        " + num + " instruction(s) executed");
                	while (num >0){
                    	execute();
                    	num--;
                	}
            		}
            	else if(ins.contains("r")){
                	//System.out.println("mips> r" );
                int last_address = address.get(list.get(list.size() - 1));
                while ( regs.get("pc") != last_address + 1  ){
                    execute();
                }
            }
            else if(ins.contains("m")){
                String [] tmp = ins.split(" ", 3);
                int start = Integer.parseInt(tmp[1]);
                int end = Integer.parseInt(tmp[2]);
                //System.out.println("mips> m " +start + " " + end);
                for(int i = start; i <= end ; i++ ){
                    System.out.println("[" + i + "] = " + memory[i]);
                }
            }
            else if(ins.equals("c")){
                //System.out.println("mips> c ");
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
                System.out.println("mips> s");
                System.out.println("        1 instruction(s) executed");
                execute();
            }
            else if(ins.contains("s")){

                String [] tmp= ins.split(" ", 2);
                int num = Integer.parseInt(tmp[1]);
                System.out.println("mips> s " + num);
                System.out.println(  "        " + num + " instruction(s) executed");
                while (num >0){
                    execute();
                    num--;
                }
            }
            else if(ins.contains("r")){
                System.out.println("mips> r" );
                int last_address = address.get(list.get(list.size() - 1));
                while ( regs.get("pc") != last_address + 1  ){
                    execute();
                }
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
    private static String v1, v2, v3;
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
    public static void execute(){
        int pc = regs.get("pc");
        String key = list.get(pc);
        String[] arr;
        String instruction ="";
        //System.out.println(key);
        if(key.indexOf(':') == -1) {
            if (key.contains(" ")) {
                arr = key.split(" ", 2);
            } else {
                arr = key.split("\\$", 2);
            }
            instruction = arr[0]; // instruction like addi
            String vals = arr[1]; // registers
            //System.out.println(instruction);
            //System.out.println(vals);
            if (instruction.equals("addi")){
                addi(vals);
                regs.put("pc", regs.get("pc") + 1);
            }
            else if (instruction.equals("add")){
                add(vals);
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
            else if(instruction.equals("lw")){
                lw(vals);
                regs.put("pc", regs.get("pc") + 1 );
            }
            else if(instruction.equals("j")){
                int pc_update = address.get(vals + ":");
                regs.put("pc", pc_update);

            }
            else{
                System.out.println("Unidentified instruction");
            }
        }
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
