/**
 * Copyright (c) 2018-2028, Chill Zhuang 庄骞 (smallchill@163.com).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.oruit.common.publisher;

import com.oruit.common.config.ApplicationContextRegister;
import com.oruit.common.event.ExceptionEvent;
import com.oruit.share.domain.BaseException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 异常日志信息事件发送
 *
 * @author wangyt
 */
public class ExceptionPublisher {

    public static void publishEvent(String method, String params, Exception ex) {
        String strs = " ";
        if (ex != null) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw, true));
            strs = sw.toString();
        }
        if (strs.length() > 4000) {
            strs = strs.substring(0, 4000);
        }
        BaseException baseException = new BaseException();
        baseException.setContent(new StringBuilder().append(params).append(strs).toString());
        baseException.setMethodname(method);
        baseException.setCreatetime(new Date());
        Map<String, Object> event = new HashMap<>(16);
        event.put("dto", baseException);
        ApplicationContextRegister.publishEvent(new ExceptionEvent(event));
    }

}
