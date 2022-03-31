import java.io.*;
import java.util.*;
import java.util.stream.StreamSupport;

public class lab2{
    private static LinkedHashMap<String, Integer> address = new LinkedHashMap<>();
    private static Map<String, String> dict = new HashMap<String, String>(){{
        //store funct for r_type
        put("add", "100000");
        put("and", "100100");
        put("or", "100101");
        put("sll", "000000");
        put("srl", "000010");
        put("sub", "100010");
        put("slt", "101010");
        put("jr", "001000");
        //store upcode for i_type
        put("addi", "001000");
        put("beq", "000100");
        put("bne", "000101");
        put("lw", "100011");
        put("sw", "101011");
        //upcode for j_type
        put("j", "000010");
        put("jal", "000011");
        //address for register
        put("0", "00000");
        put("zero", "00000");
        put("at", "00001");
        put("v0","00010");
        put("v1", "00011");
        put("a0", "00100");
        put("a1", "00101");
        put("a2", "00110");
        put("a3", "00111");
        put("t0", "01000");
        put("t1", "01001");
        put("t2", "01010");
        put("t3", "01011");
        put("t4", "01100");
        put("t5", "01101");
        put("t6", "01110");
        put("t7", "01111");
        put("s0", "10000");
        put("s1", "10001");
        put("s2", "10010");
        put("s3", "10011");
        put("s4", "10100");
        put("s5", "10101");
        put("s6", "10110");
        put("s7", "10111");
        put("t8", "11000");
        put("t9", "11001");
        put("sp", "11101");
        put("ra", "11111");

    }};
    private static HashSet<String> r_type=  new HashSet<>(Arrays.asList("add","and","or","sll","sub","slt","jr","srl"));
    private static HashSet<String> i_type = new HashSet<>(Arrays.asList("addi","beq","bne","lw","sw"));
    private static HashSet<String> j_type = new HashSet<>(Arrays.asList("j", "jal"));
    private static final String r_upcode = "000000";
    private static final String shamt = "00000";
    private static final String unused = "00000";
    public static void main(String[] args) throws IOException {
        File infile = null;
        if( 0 < args.length){
            infile = new File (args[0]);
        }
        else{
            System.err.println("No file argument: " + args);
            System.exit(0);
        }
//        infile = new File("//home/htsai04/cpe315/lab2/file.asm");
//        BufferedReader br = new BufferedReader(new FileReader(infile));
	Scanner sc = new Scanner(infile); 
	String str;
        int index = 0;
//        while ((str= br.readLine()) != null ) {
	while (sc.hasNextLine()){
	   str = sc.nextLine(); 
            if(valid(str)){
                str = str.trim();
                str = str.replace('\t', ' ');
                if(str.indexOf('#') != -1 ){
                    str = remove_comment(str);
                }
                if(str.indexOf(':')!=-1){
			if(str.isEmpty()){
		System.out.println("empty"); } 
                    String before_colon = getBefore(str);
                    String after_colon = getAfter (str);
                    after_colon = after_colon.trim();
                    address.put(before_colon, index);
                    if(!after_colon.equals("")){
                    address.put(after_colon, index);
                    index+=4;}
                }
                else if( str.indexOf(':') == -1)  {
                	if(!str.isEmpty()){	
		   		 address.put(str, index);
                   		 index += 4;}

                }
            }
        }
        for(Map.Entry<String, Integer> entry: address.entrySet()){
            //String [] strings = entry.getKey().split()
            String key = entry.getKey();
		//System.out.print (entry.getKey());  
		//System.out.println (entry.getValue());
            if(key.indexOf(':') == -1 && (!(key.isEmpty())) ) {
		//System.out.println(key);
                String[] arr;
                if (key.contains(" ")){
                    arr = key.split(" ", 2);}
                else{
                    arr = key.split("\\$", 2);}
                String instruction = arr[0];
		
                String vals = arr[1];
                vals = vals.replace('$', ' ');
                String v1,v2,v3;
                StringBuilder result = new StringBuilder();
                if(r_type.contains(instruction)) {
                    if(instruction.equals("jr")){
                        v1 = vals.trim();
                        result.append(r_upcode+ " ");
                        result.append(dict.get(v1) + " ");
                        result.append(unused+unused+unused + " ");
                        result.append(dict.get(instruction));
                        //System.out.println(result);
                    }
                    else if(instruction.equals("sll") || instruction.equals("srl")){
                        String[] vs = vals.split(",");
                        v1 = vs[0];
                        v2 = vs[1];
                        v3 = vs[2];
                        v1 = v1.trim();
                        v2 = v2.trim();
                        v3 = v3.trim();
                        result.append(r_upcode + " ");
                        result.append(unused + " ");
                        result.append(dict.get(v2) + " ");
                        result.append(dict.get(v1) + " ");
                        String shamt = decimal_to_binary(v3);
                        result.append(shamt + " ");
                        result.append(dict.get(instruction));
                        //System.out.println(result);

                    }
                    else {
                        String[] vs = vals.split(",");
                        v1 = vs[0];
                        v2 = vs[1];
                        v3 = vs[2];
                        v1 = v1.trim();
                        v2 = v2.trim();
                        v3 = v3.trim();
                        result.append(r_upcode + " ");
                        result.append(dict.get(v2) + " ");
                        result.append(dict.get(v3) + " ");
                        result.append(dict.get(v1) + " ");
                        result.append(shamt+ " ");
                        result.append(dict.get(instruction));
                        //System.out.println(result);
                    }
                }
                else if(i_type.contains(instruction)){
                    String [] vs = vals.split(",");
                    if(instruction.equals("addi")) {
                        v1 = vs[0]; v2 = vs[1]; v3 = vs[2];
                        v1= v1.trim(); v2= v2.trim();v3 =v3.trim();
                        result.append(dict.get(instruction) + " ");
                        result.append(dict.get(v2) +" ");
                        result.append(dict.get(v1) + " ");
                        String num_to_add = Integer.toBinaryString(Integer.parseInt(v3));
                        num_to_add = parse_zero(num_to_add);
                        result.append(num_to_add);
                        //System.out.println(result);
                    }
                    else if(instruction.equals("lw") || instruction.equals("sw")){
                        v1 = vs[0];
                        v2 = vs[1];
                        v1= v1.trim(); v2= v2.trim();
                        String tmp []= v2.split("\\(");
                        String offset = tmp[0];
                        String rs = tmp [1];
                        rs = rs.replace(")", "").trim();
                        result.append(dict.get(instruction)+ " ");
                        result.append(dict.get(rs) + " ");
                        result.append(dict.get(v1) + " ");
                        String num = Integer.toBinaryString(Integer.parseInt(offset));
                        num = parse_zero(num);
                        result.append(num);
                        //System.out.println(result);
                    }
                    else if (instruction.equals("beq") || instruction.equals("bne")) {
                        v1 = vs[0];
                        v2 = vs[1];
                        v3 = vs[2];
                        v1 = v1.trim();
                        v2 = v2.trim();
                        v3 = v3.trim();
                        int curr_add = address.get(key);
                        curr_add += 4;
                        int label_add = address.get(v3 + ":");
                        int offset = (label_add - curr_add) / 4;
                        String offset_s = Integer.toBinaryString(offset);
                        offset_s = parse_zero(offset_s);
                        result.append(dict.get(instruction) + " ");
                        result.append(dict.get(v1) + " ");
                        result.append(dict.get(v2) + " ");
                        result.append(offset_s);
                        //System.out.println(result);
                    }
                }
                else if (j_type.contains(instruction)){
                    int off = address.get(vals+":");
                    off /= 4;
                    String offset = Integer.toBinaryString(off);
                    offset = extend_26(offset);
                    result.append(dict.get(instruction) + " ");
                    result.append(offset);
                    //System.out.println(result);
                }
                else{
                    System.out.println("invalid instruction: " + instruction);
                }
//                System.out.print(instruction +" ");
//                System.out.print(vals +"\n");
        	 System.out.println(result);
            }
        }
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
        for(char ch: str.toCharArray()){
            if(ch == ' ') continue;
            return ch != '#';
        }
        return false;
    }
    public static String decimal_to_binary(String v){
        int num = Integer.parseInt(v);
        StringBuilder sb = new StringBuilder();
            if(num >= 16){ sb.append("1"); num -= 16; } else{ sb.append("0");}
            if(num >=8){sb.append("1");num -= 8;} else {sb.append("0"); }
            if(num >= 4){sb.append("1"); num-= 4;} else{sb.append("0"); }
            if(num>= 2) {sb.append("1"); num -=2; } else {sb.append("0"); }
            if(num >= 1){ sb.append("1"); num -=1; } else {sb.append("0");}
        return sb.toString();
    }
    public static String parse_zero(String str){
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        while(sb.length() < 16) {
            sb.insert(0, "0");
        }
        if(sb.length() > 16){
            while (sb.length() > 16){
                sb.deleteCharAt(0);
            }
        }
        return sb.toString();
    }
    public static String extend_26 (String str){
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        while (sb.length() < 26 ){
            sb.insert(0, "0");
        }
        return sb.toString();
    }
}
