package virnet.experiment.assistantapi;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import net.sf.json.JSONObject;
import virnet.experiment.operationapi.PCExecute;

public class PCConfigureInfo {

    private String cabinet_NUM;	
    private Integer equipmentNumber;
    private String[] PCConfigInfo;    //首字符串是success或fail，判断是否成功获取
    private Integer  PCNum;
    private String cabinetIp;
    
    public PCConfigureInfo(String cabinet_num,Integer equipmentNumber,String cabinetIp) {
    	this.cabinet_NUM = cabinet_num;
    	this.equipmentNumber = equipmentNumber;
    	this.PCConfigInfo = new String[5];
    	this.cabinetIp = cabinetIp;
    	this.PCNum = 4;
    }
    
    //获取PC配置信息
    public String[] getPCConfigureInfo() {

    	//第一台PC的设备序号为PCNumber
		Integer PCNumber =  equipmentNumber - PCNum + 1;
 		Integer i,k;
		for(i = PCNumber,k=1 ; i < PCNumber+PCNum ; i++,k++){
			
			System.out.println("PC机及其设备序号："+i);
			
			PCExecute pcExecute = new PCExecute(cabinet_NUM,""+i,this.cabinetIp);
			if(pcExecute.connect()){
				System.out.println("连上了："+i);
				this.PCConfigInfo[0] = 	"success";
				InputStream isFromFacility = pcExecute.getInputStream();
				
				pcExecute.execute("get");
				int count = 0;
		        while (count == 0) {
		        	try {
						count = isFromFacility.available();
					} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					}
		        }
		        //System.out.println(count);
		        byte[] PCbuffer=new byte[count];
		        int readCount = 0; // 
		        while (readCount < count) {
		        	try {
						readCount += isFromFacility.read(PCbuffer, readCount, count - readCount);
					} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					break;
					}
		        }
		        pcExecute.end();
		        
		        String PCConfigInfo = new String(PCbuffer);
		        System.out.println(PCConfigInfo);
		        
		        try{
		        	this.PCConfigInfo[k] = 	PCConfigInfo;
		        }catch(Exception e){
		        	e.printStackTrace();
		        }
			}
			else
				this.PCConfigInfo[0] = 	"fail";
		}
		return PCConfigInfo;
    }
    
    //获取PCip地址
    public String[] getIpAddress(){
    	String[] PCip = new String[5];
    	
    	//首字符串插入同样的判断操作状态的信息，要求先执行getPCConfigureInfo()
    	String[] PCConfigInfo = this.getPCConfigureInfo();
    	PCip[0] = PCConfigInfo[0];
    	
    	if(PCip[0].equals("success")){
    		for(int i = 1; i < this.PCNum+1 ; i++){     		
        		String[] ss1 = PCConfigInfo[i].split("\n");
        		//ss1[0] 为  "ip地址: XXXXXXXX"
        		String[] ss2 = ss1[0].split("：");
        		//ss2[1] 为  "XXX.XXX.XX.XX"
        		PCip[i] = ss2[1];
        		System.out.println("ip:"+PCip[i]);
        	}
    	}
    	return PCip;
    }
}
