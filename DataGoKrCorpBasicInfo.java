package scrap;

import java.io.FileWriter;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.io.CachedOutputStream;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DataGoKrCorpBasicInfo {
	
	public static String[] ITEM_NAME_KR = {"��������", "���ε�Ϲ�ȣ", "���θ�", "���ο�����", "�������ȸ���", "�����ǥ�ڼ���", "���ε�Ͻ��屸���ڵ�", "���ε�Ͻ��屸���ڵ��", "���α����ڵ�", "���α����ڵ��", "����ڵ�Ϲ�ȣ", "����������ȣ", "����⺻�ּ�", "������ּ�", "���Ȩ������URL", "�����ȭ��ȣ", "����ѽ���ȣ", "ǥ�ػ���з���", "�����������", "�������", "����ŷ��һ�������", "����ŷ��һ�����������", "����ڽ��ڻ�������", "����ڽ��ڻ�����������", "���KONEX��������", "���KONEX������������", "�߼ұ������", "����ְŷ������", "�����������", "��������ձټӱⰣ����", "���1����ձ޿��ݾ�", "ȸ�谨���θ�", "���纸���ǰ߳���", "����ֿ�����", "�������������ΰ�����ȣ", "�������������κ����Ͻ�"};
	public static String[] ITEM_NAME_EN = {"basDt","crno","corpNm","corpEnsnNm","enpPbanCmpyNm","enpRprFnm","corpRegMrktDcd","corpRegMrktDcdNm","corpDcd","corpDcdNm","bzno","enpOzpno","enpBsadr","enpDtadr","enpHmpgUrl","enpTlno","enpFxno","sicNm","enpEstbDt","enpStacMm","enpXchgLstgDt","enpXchgLstgAbolDt","enpKosdaqLstgDt","enpKosdaqLstgAbolDt","enpKrxLstgDt","enpKrxLstgAbolDt","smenpYn","enpMntrBnkNm","enpEmpeCnt","empeAvgCnwkTermCtt","enpPn1AvgSlryAmt","actnAudpnNm","audtRptOpnnCtt","enpMainBizNm","fssCorpUnqNo","fssCorpChgDtm"};

	public void getCorpBasicInfo( ) throws Exception {
		System.out.println("S----------");
		String today = "20200414";
		PrintWriter output = new PrintWriter(new FileWriter("D://scrap/corpBasicInfo_"+today+".txt"));
		
		for (int i=0;i<ITEM_NAME_KR.length;i++) {
			output.append(ITEM_NAME_KR[i]).append("|");
		}
		output.println();
		for (int i=0;i<ITEM_NAME_EN.length;i++) {
			output.append(ITEM_NAME_EN[i]).append("|");
		}
		output.println();
		
		int pageNo = 0;
		int row = 0;
		Call : for (int page=1; page<1000; page++) {
			String callUrl = "http://apis.data.go.kr/1160100/service/GetCorpBasicInfoService/getCorpOutline?pageNo=" + page + "&numOfRows=9999&resultType=json&serviceKey=n4wDF2Z0ShxHAnJC4sWU6zppNhW%2BiW%2BtTSMyYtKZD6YZ3gb4qutNOwYANP4p3oFN2X%2B37nW0IqhYd85xU9KhQg%3D%3D";
			URL url = new URL(callUrl);
			
			URLConnection con = url.openConnection();
			con.setConnectTimeout(60000);
			con.setReadTimeout(60000);
			
			InputStream is = con.getInputStream();
			CachedOutputStream cos = new CachedOutputStream();
			IOUtils.copy(is, cos);
			
			//System.out.println(new String(cos.getBytes(), "UTF-8"));
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder= factory.newDocumentBuilder();
			//InputSource in = new 
			Document doc = builder.parse(cos.getInputStream());
			
			System.out.println(doc.getTextContent());
			doc.normalizeDocument();
			
			// data.go.kr���� �����ϴ� ��ü�Ǽ�
			int totalCount = Integer.parseInt(doc.getElementsByTagName("totalCount").item(0).getTextContent());
			
			int itemCount = doc.getElementsByTagName("item").getLength();
			
			//int totalCount = doc.getElementsByTagName("totalCount")
			NodeList nl = doc.getElementsByTagName("item");
			
			for (int i=0; i < itemCount; i++) {
				row++;
				//Node nd = nl.item(i);
				NodeList childs = nl.item(i).getChildNodes();
				Map<String, String> item = new HashMap();
				for (int j=0; j<childs.getLength(); j++) {
					Node child = childs.item(j);
					String nodeName = child.getNodeName().trim();
					String text = child.getTextContent().trim();
					if (!nodeName.startsWith("#text")) {
						item.put(nodeName, text);
						//System.out.println(j+":"+child.getNodeName() + "" + child.getTextContent());
					}
				}
				for (int k=0;k<ITEM_NAME_EN.length;k++) {
					output.append(item.get(ITEM_NAME_EN[k])).append("|");
				}
				output.println();
				output.flush();
				//System.out.println(childs.getLength());
				//System.out.println(childs.item(2).getTextContent());
				//System.out.println(childs.item(2).toString());
				System.out.println(page +":" +row + "/" + totalCount);
				if (row >= totalCount) {
					break Call;
				}
			}
			
			//System.out.println(doc.getElementsByTagName("item").getLength());
		}
		if (output != null) {
			output.flush();
			output.close();
		}
		System.out.println("E----------");
	}

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		DataGoKrCorpBasicInfo cls = new DataGoKrCorpBasicInfo();
		cls.getCorpBasicInfo();
	}

}
