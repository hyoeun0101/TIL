## 웹팩이란?

- 모듈 번들러이다.
- 서로 연관있는 파일들을 하나의 자바스크립트 파일로 변환해주는 도구
- 모든 내용을 하나의 자바스크립트 파일에 담아 HTTP 요청을 최소화하여 웹 앱의 로딩 속도를 향상시킨다.

## 웹팩 데브 서버

- 빠른 웹팩 빌드를 제공해주는 Node.js 서버.
- npm run dev를 실행하면 웹팩 데브 서버가 빌드 결과물을 메모리 상에 저장하고, 이 빌드 결과물을 참조하여 로컬 서버를 띄운다. 빌드 결과물을 파일 상에 저장하지 않고, 메모리에 저장하기 때문에 파일을 쓰고 읽는 시간이 빠르다.
  - "dev": "cross-env NODE_ENV=development webpack-dev-server --open --hot"
- npm run build를 실행하면 /dist/파일이 생성되고 이 파일에는 빌드 결과물이 있다.
  - "build": "cross-env NODE_ENV=production webpack --progress --hide-modules"

## webpack.config.js - 웹팩 설정 파일

- 이 파일의 설정을 기반으로 소스 파일들을 웹팩으로 변환한다.

```javascript
var path = require("path");
var webpack = require("webpack");

module.exports = {
  entry: "./src/main.js", //웹팩으로 빌드할 파일, main.js의 내용에 따라 파일들이 웹팩으로 번들링(빌드)됨.
  output: {
    //웹팩으로 빌드한 결과물 파일 위치 지정
    path: path.resolve(__dirname, "./dist"),
    publicPath: "/dist/",
    filename: "build.js",
  },
  module: {
    //각 소스파일들을 웹팩으로 빌드할 때 js파일로 변환해줄 로더를 지정
    rules: [
      {
        test: /\.css$/, //css파일은 css-loader를 사용하여 js파일로 변환하고, vue-style-loader를 거쳐 index.html에 삽입됨
        use: ["vue-style-loader", "css-loader"],
      },
      {
        test: /\.vue$/,
        loader: "vue-loader",
        options: {
          loaders: {},
          // other vue-loader options go here
        },
      },
      {
        test: /\.js$/,
        loader: "babel-loader",
        exclude: /node_modules/,
      },
      {
        test: /\.(png|jpg|gif|svg)$/,
        loader: "file-loader",
        options: {
          name: "[name].[ext]?[hash]",
        },
      },
    ],
  },
  resolve: {
    alias: {
      vue$: "vue/dist/vue.esm.js",
    },
    extensions: ["*", ".js", ".vue", ".json"],
  },
  devServer: {
    historyApiFallback: true,
    noInfo: true,
    overlay: true,
  },
  performance: {
    hints: false,
  },
  devtool: "#eval-source-map",
};

if (process.env.NODE_ENV === "production") {
  module.exports.devtool = "#source-map";
  // http://vue-loader.vuejs.org/en/workflow/production.html
  module.exports.plugins = (module.exports.plugins || []).concat([
    new webpack.DefinePlugin({
      "process.env": {
        NODE_ENV: '"production"',
      },
    }),
    new webpack.optimize.UglifyJsPlugin({
      sourceMap: true,
      compress: {
        warnings: false,
      },
    }),
    new webpack.LoaderOptionsPlugin({
      minimize: true,
    }),
  ]);
}
```

## ES6

ES5에서는 var을 사용, 유효범위가 블록 단위로 제한되지 않았다.
ES6에서 const, let, block scope, arrow function, import, export 등장
