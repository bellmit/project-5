import React, { Component } from 'react';
import ReactDOM from 'react-dom';
import * as d3 from 'd3';

const data = [{	
	state: "United State",
	mortality: 6
},{
	state: "Alabama",
	mortality: 8.6
},{
	state: "Alaska",
	mortality: 5.8
},{
	state: "Arizona",
	mortality: 5.3
},{
	state: "Arkansas",
	mortality: 7.9
},{
	state: "California",
	mortality: 4.8
},{
	state: "Colorado",
	mortality: 5.1
},{
	state: "Connecticut",
	mortality: 4.8
},{
	state: "Delaware",
	mortality: 6.4
},{
	state: "Florida",
	mortality: 6.1
},{
	state: "Georgia",
	mortality: 7
},{
	state: "Hawaii",
	mortality: 6.4
},{
	state: "Idaho",
	mortality: 5.6
},{
	state: "Illinois",
	mortality: 6
},{
	state: "Indiana",
	mortality: 7.2
},{
	state: "lowa",
	mortality: 4.3
},{
	state: "Kansas",
	mortality: 6.5
},{
	state: "Kentucky",
	mortality: 6.4
},{
	state: "Louisiana",
	mortality: 8.7
},{
	state: "Maine",
	mortality: 7.1
},{
	state: "Maryland",
	mortality: 6.6
},{
	state: "Massachusetts",
	mortality: 4.2
},{
	state: "Michigan",
	mortality: 7.1
},{
	state: "Minnesota",
	mortality: 5.1
},{
	state: "Mississippi",
	mortality: 9.6
},{
	state: "Missouri",
	mortality: 6.5
},{
	state: "Montana",
	mortality: 5.6
},{
	state: "Nebraska",
	mortality: 5.2
},{
    state: "Nevada",
	mortality: 5.3
},{
	state: "New Hampshire",
	mortality: 5.6
},{
	state: "New Jersey",
	mortality: 4.5
},{
	state: "New Mexico",
	mortality: 5.3
},{
	state: "New York",
	mortality: 4.9
},{
	state: "North Carolina",
	mortality: 7
},{
	state: "North Dakota",
	mortality: 6
},{
	state: "Ohio",
	mortality: 7.3
},{
	state: "Oklahoma",
	mortality: 6.7
},{
	state: "Oregon",
	mortality: 4.9
},{
	state: "Pennsylvania",
	mortality: 6.7
},{
	state: "Rhode Island",
	mortality: 6.5
},
{	state: "South Carolina",
	mortality: 6.9
},{
	state: "South Dakota",
	mortality: 6.5
},{
	state: "Tennessee",
	mortality: 6.8
},{
	state: "Texas",
	mortality: 5.8 
},{
	state: "Utah",
	mortality: 5.2
},{
	state: "Vermont",
	mortality: 4.4
},{
	state: "Virginia",
	mortality: 6.2
},{
	state: "Washington",
	mortality: 4.5
},{
	state: "West Virigina",
    mortality: 7.6
},{
	state: "Wisconsin",
	mortality: 6.3 
},{
	state: "Wyoming",
	mortality: 4.8 
}];

const dims = { height: 300, width: 400, radius: 160 };
const cent = { x: (dims.width / 2), y: (dims.height / 2 + 50) };
const pie = d3.pie()
    .sort(null)
    .value(d => d.mortality);

const arcPath = d3.arc()
  .outerRadius(dims.radius)
  .innerRadius(dims.radius / 2.25);


// const projection = d3.geoAlbers()
//     .scale(950)
//     .translate([width/2, height/2]);

// const path = d3.geoPath()
//     .projection(projection);

// const colorScale = d3.scale.linear().range(["#fbeade", "#DB6919"]).interpolate(d3.interpolateLab);
// const states = topojson.feature(usa, usa.objects.units).features;
// const myData = d3.json("./data/usaMortality.json", (data) => console.log(data));

// svg.selectAll('path')
//     .data(data)
//     .attr('d', path)
//     .attr('fill', d => colour(d.myData.state))

class D3MapTest extends Component {
    constructor() {
        super();
        this.state = {
            width: dims.width,
            height: dims.height + 100
        }
        this.updateStyleAndAttrs = this.updateStyleAndAttrs.bind(this);
        this.testMap = this.testMap.bind(this);
    }

    testMap () {
        // cant read local file through browser
        d3.json("/data/world-110m")
            .then(function(data){ 
                console.log(data)
            })
            .catch(err => console.log(err));
        const projection = d3.geoEqualEarth();

        const map = d3.select("#map")
            .append('svg');

        map.attr('width', 600)
            .attr('height', 300)
            .append("path")
            .attr("d", d3.geoPath(projection))
            .attr("fill", "#fefefe");
    }

    updateStyleAndAttrs () {
        // const colour = d3.scaleLinear().range(["#fbeade", "#DB6919"]).interpolate(d3.interpolateLab);
        // const colour = d3.scaleOrdinal(d3.schemeSet2);
        // const colour = d3.scaleLinear().domain([0,1]).range(["white", "blue"]);
        // const colour = d3.scaleSequential().domain([1,10]).interpolator(d3.interpolateViridis);

        var colorInterpolator = d3.interpolateRgb("#6CF6AD", "#0371ED");
        
        var steps = 52;
        var colorArray = d3.range(0, (1 + 1 / steps), 1 / (steps - 1)).map(function(d) {
            return colorInterpolator(d)
        });
        // console.log(colorArray);
        const colour = d3.scaleOrdinal(colorArray);
        
        function arcTweenUpdate(d) {
            var i = d3.interpolate(this._current, d);
            this._current = i(1);
            return function(t) {
                return arcPath(i(t));
            }
        };
        
        d3.select("#chart")
            .selectAll('path')
            .data(pie(data))
            .attr('transform', `translate(${cent.x}, ${cent.y})`)
            .attr('class', 'arc')
            .attr('fill', d => colour(d.data.state));
        
        d3.select("#chart")
            .selectAll('path')
            .attr('d', arcPath)
            .transition().duration(750)
            .attrTween('d', arcTweenUpdate);
    }

    componentDidMount() {
        // console.log(data);
        this.updateStyleAndAttrs();
        this.testMap();
    }

    componentDidUpdate() {
        this.updateStyleAndAttrs();
        this.testMap();
    }

    render() {
        const paths = data.map(d => <path key={d.state}/>)
        return (
            <div>
                <h5>D3.js Map Test</h5>
                <div>
                    <svg
                        id='chart'
                        width={this.state.width}
                        height={this.state.height}
                        // ref={el => this.svgEl = el}
                    >{ paths }</svg>
                </div>
                <div id='map'>

                </div>
            </div>
        )
    }
}

export default D3MapTest;