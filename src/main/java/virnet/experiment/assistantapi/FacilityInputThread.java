//package virnet.experiment.assistantapi;
//
//import java.io.DataInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//
//public class FacilityInputThread extends Thread {
//	public volatile boolean stop = false;
//	InputStream isFromFacility = null;	//���豸�����������
//	String feedbackFromFacility = null;
//	
//	public FacilityInputThread(InputStream isFromFacility) {
//		this.isFromFacility = isFromFacility;
//	}
//	@Override
//	public void run() {
//		while(!stop){
//			//System.out.println("��ʼ�����豸����");
//            //byte[] buffer=new byte[1024*20];
//            int count = 0;
//        	while (count == 0&&(!stop)) {
//        		try {
//					count = isFromFacility.available();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//					stop = true;
//					break;
//				}
//        	}
//        	if(stop)
//    			break;
//        	//System.out.println(count);
//        	byte[] buffer=new byte[count];
//        	int readCount = 0; // �Ѿ��ɹ���ȡ���ֽڵĸ���
//        	while (readCount < count) {
//        		try {
//					readCount += isFromFacility.read(buffer, readCount, count - readCount);
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//					stop = true;
//					break;
//				}
//        	}
//        	feedbackFromFacility = new String(buffer);
//            System.out.print(feedbackFromFacility);
//		}
//	}
//	public void stopThread(){
//		this.stop = true;
//	}
//}
