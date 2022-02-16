
//const HTMLWebpackPlugin = require('html-webpack-plugin')
//const ExtractTextPlugin = require('extract-text-webpack-plugin')
const MiniCssExtractPlugin = require("mini-css-extract-plugin");
const OptimizeCssAssetsPlugin = require('optimize-css-assets-webpack-plugin')
//const UglifyJSPlugin = require('uglifyjs-webpack-plugin')
const webpack = require('webpack')
var debug = process.env.NODE_ENV !== "production";
var path = require('path');

// console.log('__dirname：', __dirname)
// console.log(' path.join',  path.join(__dirname, '.'))

module.exports = {

    //mode: 'production',
			
    //js file that starts it all
  //entry: './src/main/js/react/product',
  devtool: debug ? "inline-sourcemap" : false,
	entry: {
		 app: ["./src/main/js/apps/app"],
		navbar: ["./src/main/js/apps/navbar"]
	       //navbar : ["/Users/arthur/drs/drs-sys/drs-frontend/drs-webapp-v2/src/main/js/apps/navbar"]
	},
	//devtool: 'sourcemaps',    
    output: {
        path:__dirname + "/src/main/webapp/resources/bundle/",
        //path:__dirname + "/src/",
        //filename: './src/main/resources/static/built/bundle.js'
        //filename: './src/main/webapp/resources/bundle/bundle.js'
        filename: '[name].js',
        //filename: 'product.js'
        // publicPath:'/'
    },
    devServer: {
      historyApiFallback: true
    },
    //loaders, seperate js files (modules) that are bundled together
    module: {
        rules: [
                  // babel - es6 code is converted to es5
                {
                    // test: path.join(__dirname, '.'),
                    test: /\.(jsx|js)$/,
                    // include: path.resolve(__dirname, 'src'),
                    // exclude:[
                    //   /node_modules/,
                    //   /\.json$/,
                    //   // /tests/
                    // ],
                    exclude: /(node_modules)/,
                    use: {
                      loader: 'babel-loader',
                    	  options: {
                              presets: ['@babel/preset-env','@babel/preset-react']
                            }
                    }
                  },
                  // { test: /.js$/,
                  //   exclude: /node_modules/, 
                  //   use: { loader: 'babel-loader', options: { presets: ['@babel/preset-react','@babel/preset-env'] } }
                  // },
                  {
                      test: /\.(sa|sc|c)ss$/,
                      use: [
                        {
                          loader: MiniCssExtractPlugin.loader,
                          options: {
                          //  publicPath: './src/css/',
                          publicPath:'/src/main/webapp/resources/bundle/css/'
                          }
                        },
                          {
                            // This loader resolves url() and @imports inside CSS
                            loader: "css-loader",
                        }, {
                            // transform SASS to standard CSS
                            loader: "sass-loader",
                            options: {
                                implementation: require("sass")
                            }
                        }
                      ]
                    },
                    {
                      // Now we apply rule for images
                      test: /\.(png|jpe?g|gif|svg)$/,
                      use: [
                        {
                            // Using file-loader for these files
                            loader: "file-loader",
                            // In options we can set different things like format
                            // and directory to save
                            options: {
                              //outputPath: 'images'
                               outputPath: 'resources/bundle/images'
                            }
                        }
                      ]
                    },
                    // { test: /\.json$/, exclude: /(node_modules)/, use: 'json-loader' }
                    // {
                    //   test: /\.json$/,
                      
                    //   loader: 'json-loader',

                    // }
                      

                    /*
                    {
                      test: /\.css$/i,
                      use: [MiniCssExtractPlugin.loader, 'css-loader'],
                    },
                    */

                  /*

            {
            	test: /\.(scss)$/,
                use: [
                  {
                    // Adds CSS to the DOM by injecting a `<style>` tag
                    loader: 'style-loader'
                  },
                  {
                    // Interprets `@import` and `url()` like `import/require()` and will resolve them
                    loader: 'css-loader'
                  },
                  {
                    // Loader for webpack to process CSS with PostCSS
                    loader: 'postcss-loader',
                    options: {
                      plugins: function () {
                        return [
                          require('autoprefixer')
                        ];
                      }
                    }
                  },
                  {
                    // Loads a SASS/SCSS file and compiles it to CSS
                    loader: 'sass-loader'
                  }
                ]

            }
            */

        ]
    },
    // optimization: {
    //   splitChunks: {
    //     maxSize:700000,
    //     chunks: 'all',
    //     name: false
    //   }
    // },
 // plugins happen after modules have been bundled (modules being seperate js files)
    plugins: [

    	  new MiniCssExtractPlugin({
    	      // Options similar to the same options in webpackOptions.output
    	      // both options are optional
    	      filename: "./css/[name].css",
    	      chunkFilename: "./css/[id].css"
          }),


        //add minify to css
        new OptimizeCssAssetsPlugin({
            assetNameRegExp: /\.css$/,
            cssProcessor: require('cssnano'),
            cssProcessorOptions: { discardComments: {removeAll: true } },
            canPrint: true
        }),

        //reduce react size
        new webpack.DefinePlugin({ // <-- key to reducing React's size
            'process.env': {
            'NODE_ENV': JSON.stringify('production')
            }
        }),

        /* Error: webpack.optimize.UglifyJsPlugin has been removed, please use config.optimization.minimize instead.
        new webpack.optimize.UglifyJsPlugin({
            compress: {
                screw_ie8: false,
                warnings: false
            }
        }),
        */
        new webpack.optimize.AggressiveMergingPlugin()//Merge chunks
    ]




}