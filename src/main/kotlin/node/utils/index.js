// @ts-check
const { RSAUtils } = require("./rsa");
module.exports = {
  getNum(str) {
    var num = str.match(/\d+/);
    return num?.length > 0 ? num[0] : "";
  },

  encryptPwd(password, exponent, modulus) {
    const key = new RSAUtils.getKeyPair(exponent, "", modulus);
    const reversedPwd = password.split("").reverse().join("");
    return RSAUtils.encryptedString(key, reversedPwd);
  }
};


<table width="100%" align="center" style="border: 1px solid #aaa">
  <caption>
    <h2>我的课表</h2>
  </caption>
  <tr height="37px">
    <!-- 标题栏-->
    <th width="60px" bgcolor="DarkOrchid" style="color: white">节次</th>
    <th width="100px" bgcolor="DarkOrchid" style="color: white">上课时间</th>
    <th width="150px" bgcolor="DarkOrchid" style="color: white">周日</th>
    <th width="150px" bgcolor="DarkOrchid" style="color: white">周一</th>
    <th width="150px" bgcolor="DarkOrchid" style="color: white">周二</th>
    <th width="150px" bgcolor="DarkOrchid" style="color: white">周三</th>
    <th width="150px" bgcolor="DarkOrchid" style="color: white">周四</th>
    <th width="150px" bgcolor="DarkOrchid" style="color: white">周五</th>
    <th width="150px" bgcolor="DarkOrchid" style="color: white">周六</th>
  </tr>
  <tr height="37px" align="center">
    <td align="center">1</td>
    <td rowSpan="1">第1节</td>
    <td rowSpan="13" bgcolor="Cyan">我爱学习<br/>学习爱我</td>
    <td rowSpan="5"></td>
    <td rowSpan="2" bgcolor="orange">WEB应用技术<br/>@D座301</td>
    <td rowSpan="2"></td>
    <td rowSpan="2" bgcolor="hotpink">数据库原理<br/>@E407</td>
    <td rowSpan="2"></td>
    <td rowSpan="2"></td>
  </tr>
  <tr height="37px" align="center">
    <td>2</td>
    <td rowSpan="1">第2节</td>
  </tr>
  <tr height="37px" align="center">
    <td>3</td>
    <td rowSpan="1">第3节</td>
    <td rowSpan="3"></td>
    <td rowSpan="2" bgcolor="springgreen">计算机网络<br/>@E阶梯教室202</td>
    <td rowSpan="2" bgcolor="Aquamarine">计算理论导引<br/>@E208</td>
    <td rowSpan="2" bgcolor="Peru">系统分析与设计<br/>@D座406</td>
    <td rowSpan="2" bgcolor="DeepSkyBlue">中国近代社会转型<br/>@E阶梯教室101</td>
  </tr>
  <tr height="37px" align="center">
    <td>4</td>
    <td rowSpan="1">第4节</td>
  </tr>
  <tr height="37px" align="center">
    <td>5</td>
    <td rowSpan="1">第5节</td>
    <td rowSpan="3"></td>
    <td rowSpan="1"></td>
    <td rowSpan="3"></td>
  </tr>
  <tr height="37px" align="center">
    <td>6</td>
    <td rowSpan="1">第6节</td>
    <td rowSpan="2" bgcolor="SpringGreen">计算机网络<br/>@E阶梯教室202</td>
    <td rowSpan="2" bgcolor="HotPink">数据库原理<br/>@E407</td>
    <td rowSpan="2" bgcolor="Thistle">算法设计与分析<br/>@E208</td>
  </tr>
  <tr height="37px" align="center">
    <td>7</td>
    <td rowSpan="1">第7节</td>
  </tr>
  <tr height="37px" align="center">
    <td>8</td>
    <td rowSpan="1">第8节</td>
    <td rowSpan="3"></td>
    <td rowSpan="3" bgcolor="GreenYellow">企业建模与系统集成<br/>@D座503</td>
    <td rowSpan="3" bgcolor="DeepSkyBlue">中国近代社会转型<br/>@E阶梯教室101</td>
    <td rowSpan="2" bgcolor="RoyalBlue">智能系统<br/>@E407</td>
    <td rowSpan="3" bgcolor="DeepSkyBlue">中国近代社会转型<br/>@E阶梯教室101</td>
  </tr>
  <tr height="37px" align="center">
    <td>9</td>
    <td rowSpan="1">第9节</td>
  </tr>
  <tr height="37px" align="center">
    <td>0</td>
    <td rowSpan="1">第10节</td>
  </tr>
  <tr height="37px" align="center">
    <td>A</td>
    <td rowSpan="1">第11节</td>
    <td rowSpan="2" bgcolor="MediumOrchid">软件案例分析@G座304</td>
    <td rowSpan="3"></td>
    <td rowSpan="3"></td>
    <td rowSpan="3" bgcolor="DeepSkyBlue">中国近代社会转型<br/>@E阶梯教室101</td>
  </tr>
  <tr height="37px" align="center">
    <td>B</td>
    <td rowSpan="1">第12节</td>
  </tr>
  <tr height="37px" align="center">
    <td>C</td>
    <td rowSpan="1">第13节</td>
  </tr>
  <tr height="37px" align="center">
    <td>C</td>
    <td rowSpan="1">第13节</td>
  </tr>
</table>