package com.ftp.deploy.config;

import com.ftp.deploy.config.entity.ResultInfo;
import com.ftp.deploy.utils.CommonUtils;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * session 会话过期处理策略 用户被挤下以后的处理
 * @author ZERO
 */
@Component
public class MySessionExpiredStrategy implements SessionInformationExpiredStrategy {
    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {
        HttpServletResponse response = event.getResponse();
        HttpServletRequest request = event.getRequest();
        redirect(request,response);
    }

    private void redirect(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 跳转路径
        String returnPath = "/login";
        //ajax请求
        if (CommonUtils.isAjaxRequestInternal(request)){
            CommonUtils.ajaxRedirect(response,returnPath, ResultInfo.error(StatusEnum.OFFLINE.getCode(),StatusEnum.OFFLINE.getMsg()));
        }else{
            //窗口请求错误结果返回
            StringBuilder builder = new StringBuilder();
            if (response.getContentType() == null) {
                response.setContentType("text/html");
            }
            builder.append("<html><body>");
            builder.append("<script type='text/javascript'>");
            builder.append("alert('").append(StatusEnum.OFFLINE.getMsg()).append("');window.open('").append(returnPath).append("','_top')");
            builder.append("</script>");
            builder.append("</body></html>");
            response.getWriter().append(builder.toString());
        }
    }
}
