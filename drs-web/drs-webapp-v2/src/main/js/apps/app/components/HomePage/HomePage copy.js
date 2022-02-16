import React, { useEffect,useRef, useState } from 'react';
import {useSelector} from 'react-redux'
import * as d3 from 'd3';
// import { ComposableMap, Geographies, Geography,} from 'react-simple-maps';
import '../../sass/widget.scss'
import axios from 'axios';
import {DOMAIN_NAME} from '../../constants/action-types'
import {FormattedMessage} from 'react-intl'
import Select from 'react-select';
import d3Tip from 'd3-tip'
// import SkuPerformanceBarPlot from './SkuPerformanceBarPlot';
import SkuOrdersBarChart from './SkuOrdersBarChart';
import MarketplaceBarChart from './MarketplaceBarChart';
// const nobelData = [
//     {key: 'United States', value: 336},
//     {key: 'United Kingdom', value: 98},
//     {key: 'Germany', value: 79},
//     {key: 'France', value: 47},
//     {key: 'Japan', value: 21},
//     {key: 'Austria', value: 12},
//     {key: 'Switzerland', value: 23}
// ]

// const drawBarChart = () => {
//     const svg = d3.select('#nobel-bar .chart');
//         let bars = svg.selectAll('.bar').data(nobelData);
//         bars = bars.enter();
//         bars.append('rect')
//         .classed('bar',true)
//         .attr('width', 10)
//         .attr('height', function(d){return d.value;})
//         .attr('x', function(d, i){ return i * 12;});
// }

const drawLineChart = () => {
    var margin = {top: 10, right: 30, bottom: 30, left: 60},
        width = 460 - margin.left - margin.right,
        height = 400 - margin.top - margin.bottom;
    // append the svg object to the body of the page
    var svg = d3.select("#line-chart")
        .append("svg")
        .attr("width", width + margin.left + margin.right)
        .attr("height", height + margin.top + margin.bottom)
        .append("g")
        .attr("transform",
                "translate(" + margin.left + "," + margin.top + ")");

    //Read the data
    d3.csv("https://raw.githubusercontent.com/holtzy/data_to_viz/master/Example_dataset/3_TwoNumOrdered_comma.csv")
        // .then( function(d) {
        //     // When reading the csv, I must format variables:
        //     console.log(d, 'read csv')
        //     return { date : d3.timeParse("%Y-%m-%d")(d.date), value : d.value }
        // })
        .then( function(d) {
            // console.log(d, 'deal with data');
            const data = d.map(item => {
                return { date: d3.timeParse("%Y-%m-%d")(item.date), value: item.value}
            })
            // Add X axis --> it is a date format
            var x = d3.scaleTime()
                .domain(d3.extent(data, function(d) { return d.date; }))
                .range([ 0, width ]);
            svg.append("g")
                .attr("transform", "translate(0," + height + ")")
                .call(d3.axisBottom(x));

            // Add Y axis
            var y = d3.scaleLinear()
                .domain([0, d3.max(data, function(d) { return +d.value; })])
                .range([ height, 0 ]);
            svg.append("g")
                .call(d3.axisLeft(y));

            // Add the line
            svg.append("path")
                .datum(data)
                .attr("fill", "none")
                .attr("stroke", "steelblue")
                .attr("stroke-width", 1.5)
                .attr("d", d3.line()
                    .x(function(d) { return x(d.date) })
                    .y(function(d) { return y(d.value) })
                )

        })
}

const getSalesPerformance = () => {
    // axios.post(Url + '/',{
    //     headers: { 
    //       'Content-Type': 'application/json',
    //       'Access-Control-Allow-Origin': '*',
    //     }
    // }).then(res => {
    //     console.log(res.data, 'response of getSalesPerformance')
    //     const data = res.data
    //     return data
    // })

   const  data = [
       {date: '20210705', bp: 'K484', n: 288},
       {date: '20210706', bp: 'K484', n: 199 },
       {date: '20210707', bp: 'K484', n: 232 },
       {date: '20210708', bp: 'K484', n: 200 },
       {date: '20210709', bp: 'K484', n: 288 },
       {date: '20210710', bp: 'K484', n: 209 },
       {date: '20210711', bp: 'K484', n: 167},
       {date: '20210712', bp: 'K484', n: 180},
       {date: '20210705', bp: 'K487', n: 288},
       {date: '20210706', bp: 'K487', n: 112},
       {date: '20210707', bp: 'K487', n: 208},
       {date: '20210708', bp: 'K487', n: 166},
       {date: '20210709', bp: 'K487', n: 122},
       {date: '20210710', bp: 'K487', n: 199},
       {date: '20210711', bp: 'K487', n: 109},
       {date: '20210712', bp: 'K487', n: 89},
   ]
   return data
}

const drawLinePlotWithMultiGroup = () => {
    // set the dimensions and margins of the graph
    var margin = {top: 10, right: 30, bottom: 30, left: 60},
    width = 460 - margin.left - margin.right,
    height = 400 - margin.top - margin.bottom;

    // append the svg object to the body of the page
    var svg = d3.select("#line-plot-with-multi-group")
        .append("svg")
        .attr("width", width + margin.left + margin.right)
        .attr("height", height + margin.top + margin.bottom)
        .append("g")
        .attr("transform",
            "translate(" + margin.left + "," + margin.top + ")");
           
        const data = getSalesPerformance()

        // group the data: I want to draw one line per group
            var sumstat = d3.nest() // nest function allows to group the calculation per level of a factor
                .key(function(d) { return d.bp;})
                .entries(data);

        // Add X axis --> it is a date format
        var x = d3.scaleLinear()
            .domain(d3.extent(data, function(d) { return d.date; }))
            .range([ 0, width ]);
        svg.append("g")
            .attr("transform", "translate(0," + height + ")")
            .call(d3.axisBottom(x).ticks(5));

        // Add Y axis
        var y = d3.scaleLinear()
            .domain([0, d3.max(data, function(d) { return +d.n; })])
            .range([ height, 0 ]);
        svg.append("g")
            .call(d3.axisLeft(y));

        // color palette
        var res = sumstat.map(function(d){ return d.key }) // list of group names
        var color = d3.scaleOrdinal()
            .domain(res)
            .range(['#e41a1c','#377eb8','#4daf4a','#984ea3','#ff7f00','#ffff33','#a65628','#f781bf','#999999'])

        // Draw the line
        svg.selectAll(".line")
            .data(sumstat)
            .enter()
            .append("path")
            .attr("fill", "none")
            .attr("stroke", function(d){ return color(d.key) })
            .attr("stroke-width", 1.5)
            .attr("d", function(d) {
                return d3.line()
                    .x(function(d) { return x(d.date); })
                    .y(function(d) { return y(+d.n); })
                    (d.values)
            })


    //Read the data
    // d3.csv("https://raw.githubusercontent.com/holtzy/data_to_viz/master/Example_dataset/5_OneCatSevNumOrdered.csv")
    //     .then(function(data) {
    //         console.log('kkkkk')
    //         console.log(data);

    //         // group the data: I want to draw one line per group
    //         var sumstat = d3.nest() // nest function allows to group the calculation per level of a factor
    //             .key(function(d) { return d.name;})
    //             .entries(data);

    //         // Add X axis --> it is a date format
    //         var x = d3.scaleLinear()
    //             .domain(d3.extent(data, function(d) { return d.year; }))
    //             .range([ 0, width ]);
    //         svg.append("g")
    //             .attr("transform", "translate(0," + height + ")")
    //             .call(d3.axisBottom(x).ticks(5));

    //         // Add Y axis
    //         var y = d3.scaleLinear()
    //             .domain([0, d3.max(data, function(d) { return +d.n; })])
    //             .range([ height, 0 ]);
    //         svg.append("g")
    //             .call(d3.axisLeft(y));

    //         // color palette
    //         var res = sumstat.map(function(d){ return d.key }) // list of group names
    //         var color = d3.scaleOrdinal()
    //             .domain(res)
    //             .range(['#e41a1c','#377eb8','#4daf4a','#984ea3','#ff7f00','#ffff33','#a65628','#f781bf','#999999'])

    //         // Draw the line
    //         svg.selectAll(".line")
    //             .data(sumstat)
    //             .enter()
    //             .append("path")
    //             .attr("fill", "none")
    //             .attr("stroke", function(d){ return color(d.key) })
    //             .attr("stroke-width", 1.5)
    //             .attr("d", function(d) {
    //                 return d3.line()
    //                     .x(function(d) { return x(d.year); })
    //                     .y(function(d) { return y(+d.n); })
    //                     (d.values)
    //             })
    //     })
}

const drawMap = () => {

    var margin = { left:100, right: 10, top:10, bottom:150};

    var width = 700 - margin.left - margin.right;
    var height = 400 - margin.top - margin.bottom;

    var format = d3.format(",");

    // Set tooltips
    const tip = d3Tip()
        .attr('class', 'd3-tip')
        .offset([-10, 0])
        .html(function(d) {
            return "<strong>Country: </strong><span class='details'>" + d.properties.name + "<br></span>" + "<strong>Population: </strong><span class='details'>" + format(d.population) +"</span>";
        })

    var area = d3.select('#viz-map')
        .append('svg')
            .attr('width', width + margin.left + margin.right)
            .attr('height', height + margin.top + margin.bottom)
        .append('g')
            .attr('transform', 'translate(' + margin.left + ', ' + margin.top + ')');

    // The svg
    // var svg = d3.select("#map"),
    //     width = +svg.attr("width"),
    //     height = +svg.attr("height");

    // Map and projection
    var path = d3.geoPath();
    var projection = d3.geoMercator()
        .scale(110)
        .center([0,20])
        .translate([width/3, height/2]);
        // .scale(70)
        // .center([0,20])
        // .translate([width / 2, height / 2]);

    area.call(tip)

    // Data and color scale
    var data = d3.map();
    var colorScale = d3.scaleThreshold()
        .domain([100000, 1000000, 10000000, 30000000, 100000000, 500000000])
        .range(d3.schemeBlues[7]);

    // Load external data and boot
    // d3.queue()
    //     .defer(d3.json, "https://raw.githubusercontent.com/holtzy/D3-graph-gallery/master/DATA/world.geojson")
    //     .defer(d3.csv,
    //         "https://raw.githubusercontent.com/holtzy/D3-graph-gallery/master/DATA/world_population.csv", 
    //         function(d) { data.set(d.code, +d.pop); })
    //     .await(ready);

    const files = [
        "https://raw.githubusercontent.com/holtzy/D3-graph-gallery/master/DATA/world.geojson", 
        "https://raw.githubusercontent.com/holtzy/D3-graph-gallery/master/DATA/world_population.csv"
    ];
    // const popByName = d3.map();
    function row(d) {
        data.set(d.code, +d.pop)
    };

    const promises = [];

    files.map((url, index) => {
        promises.push(index ? d3.csv(url, row) : d3.json(url))
    });

    Promise.all(promises).then(function(data) {
        // console.log(data, 'promises result'); //check if all data was loaded
        //any code that depends on 'data' goes here
        ready( data[0])
    });

    

    function ready(topo) {
        let mouseOver = function(d) {
            tip.show(d, this)

            d3.selectAll(".Country")
            //   .transition()
            //   .duration(200)
              .style("opacity", .5)
              
            d3.select(this)
            //   .transition()
            //   .duration(200)
              .style("opacity", 1)
            //   .style("stroke", "black")
              .style("stroke", "white")
              .style("stroke-width", 3)
          }
        
        let mouseLeave = function(d) {
            tip.hide(d, this)

            d3.selectAll(".Country")
                // .transition()
                // .duration(200)
                .style("opacity", .8)
            d3.select(this)
                // .transition()
                // .duration(200)
                .style("stroke", "transparent")
                .style("stroke-width", 0.3)
        }
        
        // Draw the map
        area.append("g")
            .selectAll("path")
            .data(topo.features)
            .enter()
            .append("path")
                // draw each country
                .attr("d", d3.geoPath()
                .projection(projection)
                )
                // set the color of each country
                .attr("fill", function (d) {
                    d.total = data.get(d.id) || 0;
                    return colorScale(d.total);
                })
                .style("stroke", "transparent")
                .attr("class", function(d) { return "Country" } )
                .style("opacity", .8)
                .on("mouseover", mouseOver )
                .on("mouseleave", mouseLeave )
        
    }

    // function ready(topo) {
    //     console.log(topo, 'topo')
    //     // Draw the map
    //     area.append("g")
    //         .selectAll("path")
    //         .data(topo.features)
    //         .enter()
    //         .append("path")
    //             // draw each country
    //             .attr("d", d3.geoPath()
    //                 .projection(projection)
    //             )
    //             // set the color of each country
    //             .attr("fill", function (d) {
    //                 // console.log(d, 'd')
    //                 d.total = data.get(d.id) || 0;
    //                 return colorScale(d.total);
    //             });
    // }
}

const drawBarPlotHorizontal = (data) => {
    let domainMax = data[0].orders
    data.map((item, index) => {
        if (index > 0 && data[index].orders > data[index-1].orders) {
            domainMax = item.orders
        }
    })
    // set the dimensions and margins of the graph
    var margin = {top: 20, right: 30, bottom: 40, left: 90},
    width = 460 - margin.left - margin.right,
    height = 400 - margin.top - margin.bottom;

    // append the svg object to the body of the page
    var svg = d3.select("#bar-plot-horizontal")
        .append("svg")
        .attr("width", width + margin.left + margin.right)
        .attr("height", height + margin.top + margin.bottom)
        .append("g")
        .attr("transform",
            "translate(" + margin.left + "," + margin.top + ")");

            var x = d3.scaleLinear()
            .domain([0, domainMax])
            .range([ 0, width]);
        svg.append("g")
            .attr("transform", "translate(0," + height + ")")
            .call(d3.axisBottom(x))
            .selectAll("text")
            .attr("transform", "translate(-10,0)rotate(-45)")
            .style("text-anchor", "end");

        // Y axis
        var y = d3.scaleBand()
            .range([ 0, height ])
            .domain(data.map(function(d) { return d.marketplace; }))
            .padding(.1);
        svg.append("g")
            .call(d3.axisLeft(y))

        //Bars
        svg.selectAll("myRect")
            .data(data)
            .enter()
            .append("rect")
            .attr("x", x(0) )
            .attr("y", function(d) { return y(d.marketplace); })
            .attr("width", function(d) { return x(d.orders); })
            // .attr("height", y.bandwidth() )
            .attr("height", 24)
            .attr("fill", "#69b3a2")
            .attr('transform', `translate( 0,${y.bandwidth() / 2 - 12})`)


    // Parse the Data
    // d3.csv("https://raw.githubusercontent.com/holtzy/data_to_viz/master/Example_dataset/7_OneCatOneNum_header.csv")
    // .then((data) => {
    //     // Add X axis
    //     var x = d3.scaleLinear()
    //         .domain([0, 13000])
    //         .range([ 0, width]);
    //     svg.append("g")
    //         .attr("transform", "translate(0," + height + ")")
    //         .call(d3.axisBottom(x))
    //         .selectAll("text")
    //         .attr("transform", "translate(-10,0)rotate(-45)")
    //         .style("text-anchor", "end");

    //     // Y axis
    //     var y = d3.scaleBand()
    //         .range([ 0, height ])
    //         .domain(data.map(function(d) { return d.Country; }))
    //         .padding(.1);
    //     svg.append("g")
    //         .call(d3.axisLeft(y))

    //     //Bars
    //     svg.selectAll("myRect")
    //         .data(data)
    //         .enter()
    //         .append("rect")
    //         .attr("x", x(0) )
    //         .attr("y", function(d) { return y(d.Country); })
    //         .attr("width", function(d) { return x(d.Value); })
    //         .attr("height", y.bandwidth() )
    //         .attr("fill", "#69b3a2")


        // .attr("x", function(d) { return x(d.Country); })
        // .attr("y", function(d) { return y(d.Value); })
        // .attr("width", x.bandwidth())
        // .attr("height", function(d) { return height - y(d.Value); })
        // .attr("fill", "#69b3a2")

    // })

}

// const dims = { height: 100, width: 200, radius: 80 };
// const cent = { x: (dims.width / 2), y: (dims.height / 2 + 50) };

// const pie = d3.pie()
//     .sort(null)
//     .value(d => d.cost);

// const arcPath = d3.arc()
//     .outerRadius(dims.radius)
//     .innerRadius(dims.radius / 2.25);


// const geoUrl = 'https://raw.githubusercontent.com/zcreativelabs/react-simple-maps/master/topojson-maps/world-110m.json'

const Url = DOMAIN_NAME + '/pd';


//App starts here
const HomePage = () => {

   const p2m = useSelector(state => state.P2M)
   const d = useSelector(state => state.d)

   const [kcode, setKcode] = useState('K612')
//    const [mp, setMp] = useState('Amazon.com')
//    const [mpOp, setMpOp] = useState([{value:'Select...' , label: 'Select...'}])

    // const [dailyInventoryStats, setDailyInventoryStats] = useState(0)

    const [dailyOrder, setDailyOrder] = useState(0)
    const [dailySales, setDailySales] = useState(0)
    const [dailySalesQty, setDailySalesQty] = useState(0)

    // const [data, setData] = useState(donutData)
    const svgEl = useRef(null);

    useEffect(() => {
        // drawBarChart();
        drawLineChart();
        drawLinePlotWithMultiGroup();
        drawMap()
        // drawBarPlot()
        // drawBarPlotHorizontal()
        // setTimeout(function(){
        //     setData(test)
        // }, 3000);
        // setTimeout(function(){
        //     setData(donutData)
        // }, 9000);
        
    }, [])

    // testing
    // useEffect(() => {
    //     d3.csv("https://raw.githubusercontent.com/holtzy/data_to_viz/master/Example_dataset/5_OneCatSevNumOrdered.csv")
    //     .then(function(data) {
    //         console.log(data)
    //     })
    // },[])

    const getDailySalesQtyAndRev = () => {
        axios.post(Url + '/gdsqr',{
            headers: { 
              'Content-Type': 'application/json',
              'Access-Control-Allow-Origin': '*',
            }
        }).then(res => {
            console.log(res.data, 'response of getDailySalesQtyAndRev')
            setDailySalesQty(res.data.qty)
            setDailySales(res.data.rev)
        })
    }
    const getDailyOrder = () => {
        axios.post(Url + '/gdo',{
            headers: { 
              'Content-Type': 'application/json',
              'Access-Control-Allow-Origin': '*',
            }
        }).then(res => {
            console.log(res.data, 'response of getDailyOrder')
            setDailyOrder(res.data.Orders)
        })
    }
    const getDailyProductInventoryStats = () => {
        axios.post(Url + '/ginvst',{
            headers: { 
              'Content-Type': 'application/json',
              'Access-Control-Allow-Origin': '*',
            }
        }).then(res => {
            console.log(res.data, 'response of getDailyProductInventoryStats')
            // setDailyOrder(res.data.qty)
        })
    }

    const getKeyProductBaseRetailPrice = () => {
        axios.post(Url + '/gkprp', {
            // kCode: d.u.cid
            kCode: kcode
          },{
            headers: { 
              'Content-Type': 'application/json',
              'Access-Control-Allow-Origin': '*',
            }
        }).then(res => {
            console.log(res.data, 'response of getKeyProductBaseRetailPrice')
            
        })
    }

    // const getDetailPageSalesTraffic = () => {
    //     axios.post(Url + '/gpst', {
    //         // kCode: d.u.cid
    //         kCode: kcode,
    //         mp: mp,
    //       },{
    //         headers: { 
    //           'Content-Type': 'application/json',
    //           'Access-Control-Allow-Origin': '*',
    //         }
    //     }).then(res => {
    //         console.log(res.data, 'response of getDetailPagesSalesTraffic')
            
    //     })
    // }

    // const getKeyProductSinceLastSettlementOrder = () => {
    //     axios.post(Url + '/gkplseor', {kCode: kcode, mp: mp},{
    //         headers: {
    //             'Content-Type': 'application/json',
    //             'Access-Control-Allow-Origin': '*'
    //         }
    //     }).then(res => {
    //         console.log(res.data, 'response of getKeyProductSinceLastSettlementOrder')
    //     })
    // }

    const getMarketplaceOrders = () => {
        const data = [
            {marketplace: 'Amazon.com', orders: 20},
            {marketplace: 'Amazon.it', orders: 18}
        ]
        // drawBarPlot(data)
        drawBarPlotHorizontal(data)
    }
    // const getKeyProductLastSevenDaysOrder = () => {
    //     axios.post(Url + '/gkplsor', {kCode: kcode, mp: mp},{
    //         headers: {
    //             'Content-Type': 'application/json',
    //             'Access-Control-Allow-Origin': '*'
    //         }
    //     }).then(res => {
    //         console.log(res.data, 'response of getKeyProductLastSevenDaysOrder')
    //     })
    // }

    useEffect(() => {
        getDailySalesQtyAndRev()
        getDailyOrder()
        getDailyProductInventoryStats()
        getKeyProductBaseRetailPrice()
        // getDetailPageSalesTraffic()
        // getKeyProductSinceLastSettlementOrder()
        // getKeyProductLastSevenDaysOrder()
        getMarketplaceOrders()
        // getSkuPerformance(mp, {init: true})
        // handleMpOptions()
    }, [])

    // const handleMpOptions = () => {
    //     axios.post(Url + '/gkplseor', {kCode: kcode},{
    //         headers: {
    //             'Content-Type': 'application/json',
    //             'Access-Control-Allow-Origin': '*'
    //         }
    //     }).then(res => {
    //         if(res !== null) {
    //             const result = res.data
    //             const marketplace = result.map(item => {
    //                 return  item.marketplace
    //             })
    //             const distinctMarketplace = [...new Set(marketplace)]
    //             console.log(distinctMarketplace)
    //             const options = distinctMarketplace.map(item => {
    //                 return {value: item, label: item}
    //             })
    //             setMpOp(options)
    //             console.log(options)
    //         }
    //     })
    // }


    // const getSkuPerformance = (selectMp, param) => {
        
    //     axios.post(Url + '/gkplseor', {kCode: kcode, mp: selectMp},{
    //         headers: {
    //             'Content-Type': 'application/json',
    //             'Access-Control-Allow-Origin': '*'
    //         }
    //     }).then(res => {
    //         // console.log(res.data, 'response of getKeyProductSinceLastSettlementOrder')
    //         if(res !== null) {
    //             const skuPerformanceData = res.data.map(item => {
    //                 return { sku: item.sku, totalOrder: item.totalOrder }
    //                 // {marketplace: "Amazon.com", bpCode: "BP-K612-8BSMAP0118D0700", sku: "K612-AA5", totalOrder: 11}
    //             })
    //             // drawDonutChart(skuPerformanceData )
    //             if (param.init) {
    //                 drawBarPlot(skuPerformanceData, !param.init)
    //             } else {
    //                 drawBarPlot(skuPerformanceData, !param.init)
    //                 // updateBarPlot(skuPerformanceData,  )
    //             }
    //         }
    //     })
        
    // }

    // const drawDonutChart = (data) => {
    //     console.log(data)
    //     const colour = d3.scaleOrdinal(['#FBC2A2','#7BF7C9','#F9FCBC','#ECA9FF','#E488FC']);
    
    //     function arcTweenUpdate(d) {
    //       var i = d3.interpolate(this._current, d);
    //       this._current = i(1);
    //       return function(t) {
    //         return arcPath(i(t));
    //       }
    //     };
    
    //     d3.select(svgEl.current)
    //       .selectAll('path')
    //       .data(pie(data))
    //       .attr('transform', `translate(${cent.x}, ${cent.y})`)
    //       .attr('class', 'arc')
    //       .attr('fill', d => colour(d.data.name));
    
    //     d3.select(svgEl.current)
    //       .selectAll('path')
    //       .attr('d', arcPath)
    //       .transition().duration(750)
    //       .attrTween('d', arcTweenUpdate);
    // }

    // const paths = data.map(d => <path key={d.name}/>)
  
    

    // const onSelectSupplier = () => {
    //     console.log('onselect supplier')
    // }
    // const onSelectBP = () => {
    //     console.log('onselect bp')
    // }
    // const onSelectMp = (e) => {
    //     console.log(e)
    //     setMp(e.value)
    //     getSkuPerformance(e.value, {init: false})
    // }

    return (
        <div className="home-container">
            <div className="pannel-header">
                <div style={{width: '100%',display: 'flex', flexDirection: 'column', alignItems: 'left'}}>
                    <h1 style={{marginRight: '12px'}}>Key Product Stats</h1>
                    <div className="card-content-info">
                        <span>Time Period : Since Last Settlement</span>
                        <span>Last Updated : 07/13/2021 00:00</span>
                    </div>
                </div>
                {/* {!d.u.isSp
                    ?   <div style={{display:'flex', alignItems: 'left'}}>
                            <h1 style={{marginRight: '12px'}}>
                                <FormattedMessage id="product.supplier"/>
                            </h1>
                            <Select
                                className='drs-selector'
                                //placeholder={productState.currentBP}
                                // options={d.supplierSelectOptions}
                                //value={productState.currentBP}
                                onChange={(e) => onSelectSupplier(e)}
                            />
                        </div>
                    : null
                } */}
            </div>
            {/* <h4 style={{paddingTop: '5%', paddingLeft: '4%'}}>Dashboard</h4> */}
            <div className="widget-wrapper">
                <div className="data-card-wrapper">
                    <div className="data-card-s">
                        <p className="data-card-title">訂單</p>
                        <p className="data-card-content">{dailyOrder}</p>
                        {/* <div className="data-card-info">
                            <span className="data-card-static static-grow-up">2.3%</span>
                            <span className="data-card-description">than last month</span>
                        </div> */}
                    </div>
                    <div className="data-card-s">
                        <p className="data-card-title">銷售額</p>
                        {/* <p className="data-card-content">{this.props.rows.length}</p> */}
                        <p className="data-card-content">{dailySales}</p>
                        {/* <div className="data-card-info">
                            <span className="data-card-static static-grow-up">2021.07.08</span>
                            <span className="data-card-description">最新更新時間 07/08/2021 00:00:00</span>
                        </div> */}
                    </div>
                    <div className="data-card-s">
                        <p className="data-card-title">銷售量</p>
                        <p className="data-card-content">{dailySalesQty}</p>
                        {/* <div className="data-card-info">
                            <span className="data-card-static static-drop-down">1.3%</span>
                            <span className="data-card-description">than last month</span>
                        </div> */}
                    </div>
                    <div className="data-card-s">
                        <p className="data-card-title">Welcome! This is data info dashboard....</p>
                        <p >Please take a look at the notifications. It seems you have 5 notifications.</p>
                        
                    </div>
                </div>
            </div>
            {/* <div className="widget-wrapper">
                <div className="data-card-wrapper">
                    <div className="data-card-m">
                        <p className="data-card-title">Brand Overview</p>
                        <p className="data-card-title">長期銷售趨勢</p>
                        
                        <div id="line-plot-with-multi-group"></div>
                    </div>
                    <div className="data-card-s">
                        <p className="data-card-title">各別產品表現</p>
                        <div id='donut-chart'>
                            <svg
                                width={ dims.width}
                                height={dims.height + 100}
                                ref={svgEl}
                            >{ paths }</svg>
                        </div>
                        <ul className='chart-legend'>
                            { data.map(d => 
                               
                                <li className='legend-list'>
                                    <div className='legend-list-column-left'>
                                        <div className={`legend-circle ${d.color}`}></div>
                                        <span className='legend-category'>{d.name}</span>
                                    </div>
                                    <div className='legend-list-column-middle'>
                                        <span className='legend-cost'>{d.cost}</span>
                                    </div>
                                    <div className='legend-list-column-right'>
                                        <span className='legend-percentage'>{d.percentage}</span>
                                        <span className='legend-percentage'>%</span>
                                    </div>
                                </li>
                            )}
                        </ul> 
                    </div>
                </div>
            </div> */}
            
            {/* <div className="widget-wrapper">
                <div className="data-card-wrapper">
                    <div className="data-card" style={{width: '100%', display: 'flex', alignItems: 'center', justifyContent: 'space-around'}}>
                        <div id="viz-map"></div>
                        <div>
                            <p className="data-card-title">市場地區表現</p>
                            <div id="bar-plot-horizontal"></div>
                        </div>
                        
                    </div>
                </div>
            </div> */}
            <div className="widget-wrapper">
                <div className="data-card-wrapper">
                    <div className="data-card" style={{width: '40%', height: '450px'}}>
                        <MarketplaceBarChart
                            kcode={kcode}
                        />
                    </div>
                    <div className="data-card" style={{width: '56%', height: '450px'}}>
                        <SkuOrdersBarChart
                            kcode={kcode}
                        />
                    </div>
                </div>
            </div>
            {/* <div className="widget-wrapper">
                <div className="data-card-wrapper">
                    <div className="data-card-ms">
                        <p className="data-card-title">各產品庫存量</p>
                        <div id="bar-plot"></div>
                    </div>
                    <div className="data-card-ms">
                        <p className="data-card-title">長期庫存趨勢</p>
                        <div id="line-chart"></div>
                    </div>
                </div>
            </div> */}

            {/* <div style={{width: '50%'}}>
                <ComposableMap>
                    <Geographies geography={geoUrl}>
                        {({geographies})  => geographies.map(geo => 
                            <Geography key={geo.rsmKey} geography={geo}/>
                        )}
                    </Geographies>
                </ComposableMap>
            </div> */}
            
        </div>
    )
}

export default (HomePage);
