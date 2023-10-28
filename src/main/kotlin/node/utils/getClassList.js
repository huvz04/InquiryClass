const classList = require("./class_row.json");
const fs = require("fs");
for (const key in classList) {
  classList[key] = classList[key].map((item) => ({
    bj: item.bj,
    zyh_id: item.zyh_id,
    bh_id: item.bh_id,
  }));
  console.log(key, classList[key].length);
}

fs.writeFileSync(`${__dirname}/classList.json`, JSON.stringify(classList));
