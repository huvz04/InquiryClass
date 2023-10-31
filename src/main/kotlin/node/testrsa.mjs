import { encryptPwd } from "./utils/index.js";

const modulus =
    'b9adfe238a34fbd6cf592f2ce794c46d103cee9bccb9f8a9e446a17d3872cc2e144897cef7aa317240f3ba7fb162c411775f2a5968991d781694ee548fce626d';
const exponent = '10001';

console.log('------------- 生成加密的 -------------');
const str = '112312312';
console.log('str:', str);
const encryptR = encryptPwd(str, exponent, modulus);
console.log('encryptR:', encryptR);
// console.log('------------- 解密Java生成的 -------------');
// var pwd64 = "9d21209bd5e4b4ed843bf81a379851f0329687e8b88318ac2e1a7913aa83cc421b6956bd57fbf70792eeab7535e18797eea17b3bfc1912178b0b34cf72de3832cb231491265bba2b3d200d9c9eeaa7669faa572e1685c86a12ca30f09f265b739b4ebebf72e6fb2198dac0e4493a65d609beb4c30e3dc6357ce9993269c4700a"
// var decryptR2 = decryptRSA(pwd64, exponent, modulus)
// console.log('decryptR: %o', decryptR2);