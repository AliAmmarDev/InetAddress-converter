import java.util.*;
import java.net.*;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class cwk1{
  private Scanner kbdReader = null;
  private static Pattern VALID_IPV4_PATTERN = null;
  private static Pattern VALID_IPV6_PATTERN = null;
  private static final String ipv4Pattern = "(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])";
  private static final String ipv6Pattern = "([0-9a-f]{1,4}:){7}([0-9a-f]){1,4}";
	private InetAddress inet = null;
  private static List<String[]> IPv4Addresses = new ArrayList<String[]>();


  //constrcutor
  public InetAddressConverter(){
    kbdReader = new Scanner(System.in);
  }


  public void continuousScan() throws IOException{
    System.out.println("\nEnter Host name or IP Address\n");
		String cmd = "";
    int validCounter = 0;
    //take continious input
    while(true){
      cmd = kbdReader.nextLine();
      //split given ips/ hosts
      String[] ipAmount = cmd.split("\\s+");
      //stop taking input
			if(cmd.equals("exit")){
        break;
			}
      //if input is given
			if(cmd.length() > 0){
        validCounter = 0;
        //create arraylist
        List<String[]> IPv4Addresses = new ArrayList<String[]>();
        int inputLength = ipAmount.length;
        //check if ips are valid
        for(int counter = 0; counter < inputLength; counter++){
          validCounter += validIp(ipAmount[counter]);
        }
        //obtain detials of given ips/hosts
        for(int counter = 0; counter < inputLength; counter++){
          resolve(ipAmount[counter], validCounter);
        }
        //if more than 1 valid ip/ host is given
        if(validCounter > 1){
          commonAddresses();
          //clear arraylist
          IPv4Addresses.clear();
        }
        System.out.println("\n==============================\n");
			}
    }
  }
  //check if ip/ host given is valid IPv4 or IPv6
  public int validIp(String host) throws IOException{
    try{
      VALID_IPV4_PATTERN = Pattern.compile(ipv4Pattern);
      VALID_IPV6_PATTERN = Pattern.compile(ipv6Pattern);
      inet = InetAddress.getByName(host);
      Matcher m1 = VALID_IPV4_PATTERN.matcher(inet.getHostAddress().toString());
      //if valid IPv4
      if(m1.matches()){
        return 1;
      }
    }
    //invalid ip/ host
    catch( UnknownHostException e){
    }
    return 0;
  }

  public void resolve(String host, int inputLength) throws IOException{
    try{

      inet = InetAddress.getByName(host);

			System.out.println( "Host name : " + inet.getHostName());
			System.out.println( "IP Address: " + inet.getHostAddress());
	    System.out.println( "Canonical host name: " + inet.getCanonicalHostName());
      if(inet.isReachable(1000)){
        System.out.println( "IP Address reachable: YES ");
      }
      else{
        System.out.println( "IP Address reachable: NO ");
      }
      //ipv4 pattern
      Matcher m1 = VALID_IPV4_PATTERN.matcher(inet.getHostAddress().toString());
      //ipv6 pattern
      Matcher m2 = VALID_IPV6_PATTERN.matcher(inet.getHostAddress().toString());
      //if matches ipv4 pattern
      if(m1.matches()){
	      System.out.println( "Valid IPv4");
        //if atleast 2 valid ipv4 are given
        if(inputLength > 1){
          //split ip address into array
  		    String[] ipv4Parts = inet.getHostAddress().split("[.]");
          //add array to arraylist
  		     IPv4Addresses.add(ipv4Parts);
        }
      }
      //if matches ipv6 pattern
      if(m2.matches()){
      	System.out.println( "Valid IPv6");
      }
      System.out.println();
    }
    catch( UnknownHostException e){
			System.out.printf("Invalid entry %s\n\n", host);
		}
  }


  public void commonAddresses(){
    //get first ip address
    String[] temp1  = IPv4Addresses.get(0);
		String[] temp2;
    //iterate across parts of ip address
		for(int j = 0; j < 4; j++){
      //iterate through remaining ip addresses
			for (int i = 1; i < IPv4Addresses.size(); i++){
        //get next ip address
				temp2 = IPv4Addresses.get(i);
        //if not equal parts found
				if(!(temp1[j].equals(temp2[j]))){
          //if first part of the ip address
					if(j == 0){
						System.out.print("IPv4 Heirachy:\nNo Heirachy\n");
						return;
					}
          //if not first part of the ip print remaining parts with *
					printCommon(j);
					return;
				}
	    }
		}
    //print ip address
		printCommon(4);
  }

  private static void printCommon(int i){
		System.out.println("IPv4 Heirachy");
    //get first address
		String[] array  = IPv4Addresses.get(0);
    //iterate accross parts of ip address
		for(int j = 0; j < 4; j++){
			if(j < i){
				System.out.print(array[j]);
			}
			else{
				System.out.print("*");
			}
			if(j < 3){
				System.out.print(".");
			}
		}
		System.out.print("\n");
	}


  public static void main(String[] args) throws IOException{
		cwk1 kbd = new cwk1();
    int validCounter = 0;
    if(args.length > 0){
			int i =0;
      //if arguments are given
	    while(i < args.length){
        validCounter += kbd.validIp(args[i]);
	      kbd.resolve( args[i], args.length);
	      i++;
	    }
      if(validCounter > 1){
        kbd.commonAddresses();
      }
    }
		kbd.continuousScan();

	}

}
