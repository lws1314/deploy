package com.ftp.deploy.utils;

import java.io.File;
import java.util.Map;
import java.util.Optional;

import com.ftp.deploy.controller.FtpController;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * @author ZERO
 */
public class JDomXml {

	/**
	 * 生成xml方法
	 * @param serverId   服务id
	 * @param savePath 文件保存的路径
	 * @param fileName 文本名
	 * @return true 成功 false  失败
	 */
	public static boolean createXml(String serverId, String savePath, String fileName, Map<String, Object> param){
		try {
			String serverName = (String)param.get(FtpController.SERVER_NAME);
			String runParam = (String) param.get(FtpController.RUN_PARAM);
			if (StringUtils.isEmpty(serverName)){
				param.put(FtpController.SERVER_NAME,serverId);
				serverName = serverId;
			}
			// 创建解析器工厂
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = factory.newDocumentBuilder();
			Document document = db.newDocument();

			// 不显示standalone="no"
			document.setXmlStandalone(false);
			//根节点
			Element service = document.createElement("service");

			//创建子元素  设置属性
			Element id = document.createElement("id");
			id.setTextContent(serverId);

			Element name = document.createElement("name");
			name.setTextContent(serverName);

			Element description = document.createElement("description");
			description.setTextContent(serverName+"@智统科技");

			Element env = document.createElement("env");
			env.setAttribute("name","JAVA_HOME");
			env.setAttribute("value","%JAVA_HOME%");

			Element executable = document.createElement("executable");
			executable.setTextContent("java");

			Element arguments = document.createElement("arguments");
			String jar = "\"%BASE%\\"+serverId+".jar\"";
			if (StringUtils.isEmpty(runParam)){
				arguments.setTextContent("-jar "+jar+"");
			}else{
				String replace = runParam.replace("[JAR]", jar);
				if (replace.contains("'")){
					replace = replace.replaceAll("'","\"");
				}
				arguments.setTextContent(replace);
			}
			param.put(FtpController.RUN_PARAM,arguments.getTextContent());

			Element startmode = document.createElement("startmode");
			//设置开机启动
			startmode.setTextContent("Automatic");

			Element logpath = document.createElement("logpath");
			logpath.setTextContent("%BASE%\\console");

			Element logmode = document.createElement("logmode");
			logmode.setTextContent("rotate");

			//添加节点
			service.appendChild(id);
			service.appendChild(name);
			service.appendChild(description);
			service.appendChild(env);
			service.appendChild(executable);
			service.appendChild(arguments);
			service.appendChild(startmode);
			service.appendChild(logpath);
			service.appendChild(logmode);

			// 将bookstore节点（已包含book）添加到dom树中
			document.appendChild(service);

			// 创建TransformerFactory对象
			TransformerFactory tff = TransformerFactory.newInstance();
			// 创建 Transformer对象
			Transformer tf = tff.newTransformer();
			// 输出内容是否使用换行
			tf.setOutputProperty(OutputKeys.INDENT, "yes");
			// 创建xml文件并写入内容

			boolean mkdirs = true;
			File  newFile = new File(savePath);
			if (!newFile.exists()) {
				mkdirs = newFile.mkdirs();
			}
			if (mkdirs){
				tf.transform(new DOMSource(document), new StreamResult(new File(savePath,fileName)));
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
}
