// @ts-check
const _request = require("request");
const cheerio = require("cheerio");
const classList = require("./utils/classList.json");
const { encryptPwd, getNum } = require("./utils");
const jar = _request.jar();
const request = _request.defaults({ jar });
const UserAgent =
  "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36 Edg/119.0.0.0";

class JhcInquiry {
  xnm = "2023";
  xqm = "3";
  constructor(username, password, options = {}) {
    this.username = username;
    this.password = password;
    for (const key in options) {
      this[key] = options[key];
    }
    this.initial();
  }
  initial() {
    console.log("----------------initial----------------");
    request(
      "https://rz.jhc.cn/zfca/login",
      {
        qs: {
          service: "https://jwglxt.jhc.cn/sso/zfiotlogin",
        },
      },
      (err, res, body) => {
        try {
          if (err) throw Error(err);
          const $ = cheerio.load(body);
          this.execution = $("input[name=execution]").attr("value");
          this.getPubKey();
        } catch (error) {
          console.log("An error occurred in initial because: ", error);
        }
      }
    );
  }
  getPubKey() {
    console.log("----------------getPubKey----------------");
    request(
      "https://rz.jhc.cn/zfca/v2/getPubKey",
      undefined,
      (err, res, body) => {
        try {
          if (err) throw Error(err);
          const json = JSON.parse(body);
          this.modulus = json.modulus;
          this.exponent = json.exponent;
          this.login();
        } catch (error) {
          console.log("An error occurred in getPubKey because: ", error);
        }
      }
    );
  }
  login() {
    console.log("----------------login----------------");
    request(
      "https://rz.jhc.cn/zfca/login",
      {
        followAllRedirects: true,
        method: "POST",
        form: {
          username: this.username,
          password: encryptPwd(this.password, this.exponent, this.modulus),
          execution: this.execution,
          _eventId: "submit",
          authcode: "",
        },
        headers: {
          "User-Agent": UserAgent,
        },
      },
      (err, res, body) => {
        try {
          if (err) throw Error(err);
          // this.queryCourse("计算机211");
          this.loginStatus = true;
        } catch (error) {
          console.log("An error occurred in login because: ", error);
        }
      }
    );
  }
  async queryCourse(className) {
    return new Promise((resolve, reject) => {
      const term = getNum(className).slice(0, 2).padStart(4, "20");
      let queryClass = classList[term]?.find((item) => {
        return item.bj == className;
      });
      if (!queryClass) {
        reject(`未找到名为${className}的班级`);
        return;
      }
      console.log(queryClass);
      request(
        "https://jwglxt.jhc.cn/jwglxt/kbdy/bjkbdy_cxBjKb.html",
        {
          method: "POST",
          qs: {
            gnmkdm: "N214505",
          },
          form: {
            xnm: this.xnm,
            xqm: this.xqm,
            njdm_id: term,
            zyh_id: queryClass.zyh_id,
            bh_id: queryClass.bh_id,
            tjkbzdm: "1",
            tjkbzxsdm: "0",
          },
        },
        (err, res, body) => {
          try {
            if (err) throw Error(err);
            const json = JSON.parse(body);
            resolve(json);
          } catch (error) {
            this.initial();
            reject(`An error occurred in queryCourse because: ${error}`);
          }
        }
      );
    });
  }
}

module.exports = JhcInquiry;

// const jhcInquiry = new JhcInquiry("202110101550022", "*******");

// const express = require("express");
// const app = express();
// app.get("/", (req, res) => {
//   jhcInquiry
//     .queryCourse(req.query.className)
//     .then((result) => {
//       res.send(result);
//     })
//     .catch((err) => {
//       res.status(500).send(err);
//     });
// });
// app.listen(3000, () => {
//   console.log("Example app listening on port 3000!");
// });
