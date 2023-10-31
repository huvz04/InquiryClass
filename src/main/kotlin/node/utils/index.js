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