import React, {useState, useEffect, useRef} from 'react';
import { scaleLinear, scaleBand, select, axisBottom, axisLeft, max, format, descending, ascending} from 'd3';
import axios from 'axios';
import {DOMAIN_NAME} from '../../constants/action-types'
import { withStyles } from '@material-ui/core/styles';
import { blue } from '@material-ui/core/colors';
import Switch from '@material-ui/core/Switch';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import { tip as d3tip } from "d3-v6-tip";

const Url = DOMAIN_NAME + '/pd';

const BlueSwitch = withStyles({
    switchBase: {
      color: blue[800],
      '&$checked': {
        color: blue[900],
      },
      '&$checked + $track': {
        backgroundColor: blue[900],
      },
    },
    checked: {},
    track: {},
  })(Switch);


const MarketplaceBarChart = ({data}) => {

    const svgRef = useRef();
    const [width, setWidth] = useState(460)
    const [height, setHeight] = useState(380)
    const [isSorted, setIsSorted] = useState(false)

    const margin = {top: 20, right: 30, bottom: 60, left: 90};
    const w = width - margin.left - margin.right;
    const h = height - margin.top - margin.bottom;

    let xScale = scaleLinear()
        .domain([0, max(data, d => d.orders)])
        .range([0, w]);

    let yScale = scaleBand()
        .range([ 0, h ])
        .domain(data.map(function(d) { return d.marketplace; }))
        .paddingInner(data.length < 5 ? 0.5 : 0.1)
        .paddingOuter(data.length < 5 ? 0.5 : 0.1)

    let colorScale = scaleLinear()
        .domain([0, data.length])
        .range(['rgba(19, 78, 168, 1)', 'rgba(19, 78, 168, 0.2)'])
        // .range(['rgba(9,65,115, 1)', 'rgba(64,140,245,1)'])
        // .range(['rgba(13, 133, 252, 1)', 'rgba(13, 133, 252, 0.2)'])
    
    let tip = d3tip()
        .attr('class', 'drs-d3-tip')
        .offset([-10, 0])
        .html(function(e,d) {
            return "<span>Total Order:" + d.orders + "</span>";
        })
        
    const handleMouseEnter = ( e, d)=> {
        tip.show(e, d)
        select(e.target).attr('opacity', '0.9')
    }
    const handleMouseLeave = (e, d) => {
        tip.hide(e, d)
        select(e.target).attr('opacity', '1')
    }

    const renderChart = () => {
        const svg = select(svgRef.current);
        const group = svg.append('g')
            .attr('class', 'rect-group')
            .attr('transform', `translate(${margin.left},${margin.top} )`)
            .call(tip)  

        group.selectAll("rect")
            .data(data)
            .enter()
            .append("rect")
            .attr("x", xScale(0) )
            .attr("y", function(d) { return yScale(d.marketplace); })
            .attr("width", function(d) { return xScale(d.orders); })
            .attr("height", yScale.bandwidth() )
            .attr("fill", (d,i)=> colorScale(i))
            .on('mouseenter', (e,d) => {
                handleMouseEnter(e,d)
            })
            .on('mouseleave', (e,d) => {
                handleMouseLeave(e,d)
            })
            // .attr("fill", function(d) {
            //     return "rgb("+ Math.round(d.orders * 4) + ",112," + Math.round(d.orders+230 ) + ")";
            // });
       
        const xAxis = axisBottom(xScale).tickFormat(format('d'));
        if (xScale.domain() [1] < 10) {
            xAxis.ticks(xScale.domain()[1])
        }
        
        svg.select(".x-axis")
            .attr("transform",`translate(${margin.left}, ${h + margin.top + 5})`)
            .call(xAxis)

        const yAxis = axisLeft(yScale);
        svg.select('.y-axis')
            .attr("transform", `translate(${margin.left - 5}, ${margin.top})`)
            .call(yAxis)
            .selectAll("text")
            .attr("transform", "translate(-10,0)rotate(-45)")
            .style("text-anchor", "end");

        svg.append('text')
            .attr('class', 'x-label')
            .attr('x', width / 2 + 30)
            .attr('y', height - 15 )
            .attr('text-anchor', 'middle')
            .text('Total Orders')
    }

    const updateBar = () => {
        const svg = select(svgRef.current);
        const group = svg.select('.rect-group')
        const update = group.selectAll('rect').data(data)
        const updatexAxis = svg.select('.x-axis');
        const updateyAxis = svg.select('.y-axis');
        
        colorScale.domain([0, data.length])
        xScale.domain([0, max(data, d => d.orders)])
        yScale.domain(data.map(function(d) { return d.marketplace; }))

        update.enter()
            .append("rect")
            .attr("x", xScale(0) )
            .attr("y", function(d) { return yScale(d.marketplace); })
            .attr("width", 0)
            .attr("height", yScale.bandwidth())
            .attr("fill", (d, i) => colorScale(i))
            // .attr("fill", function(d) {
            //     return "rgb("+ Math.round(d.orders * 4) + ",112," + Math.round(d.orders+230 ) + ")";
            // })
            .merge(update)
            .transition().duration(1500)
            .attr("x", xScale(0) )
            .attr("y", function(d) { return yScale(d.marketplace); })
            .attr("width", function(d) { return xScale(d.orders); })
            .attr("height", yScale.bandwidth())
        
        const rects = svg.selectAll('rect')
            .on('mouseenter', (e,d) => {
               handleMouseEnter(e, d)
            })
            .on('mouseleave', (e,d) => {
               handleMouseLeave(e, d)
            })
        svg.call(tip)
        
        const xAxis = axisBottom(xScale).tickFormat(format('d'));
        if (xScale.domain() [1] < 10) {
            xAxis.ticks(xScale.domain()[1])
        }
        updatexAxis.enter()
            .merge(updatexAxis)
            .attr("transform",`translate(${margin.left}, ${h + margin.top + 5})`)
            .call(xAxis)

        const yAxis = axisLeft(yScale);
        updateyAxis.enter()
            .merge(updateyAxis)
            .attr("transform", `translate(${margin.left - 5}, ${margin.top})`)
            .call(yAxis)
            .selectAll("text")
            .attr("transform", "translate(-10,0)rotate(-45)")
            .style("text-anchor", "end");
        
        updatexAxis.exit()
            .transition().duration(750).remove()

        updateyAxis.exit()
            .transition().duration(750).remove()
        
        update.exit()
            .attr('fill', '#dedede')
            .transition().duration(750)
            .attr('x',xScale(0))
            .attr('width', 0)
            .remove();
    }

    useEffect(() => {
        if (data.length > 0) updateBar() //rerender chart when data changes
    }, [data])

    useEffect(() => {
        renderChart() //initially render chart
    },[])

    const toggleChecked = () => {
        const changedData = data;
        if (isSorted) {
            changedData.sort((a, b) => {
                return ascending(a.marketplace, b.marketplace)
            })
            setIsSorted(false)
        } else {
            changedData.sort((a, b) => {
                return descending(a.orders, b.orders)
            })
            setIsSorted(true)
        }
        setData(changedData)
        updateBar()
    };

    return (
        <div>
            <div style={{display: 'flex', alignItems: 'center', justifyContent: 'space-between'}}>
                <p className="data-card-title">市場地區表現</p>
                <div className="data-card-control">   
                    <FormControlLabel
                        control={
                            <BlueSwitch size="small" checked={isSorted} onChange={toggleChecked} />
                        }
                        label={isSorted ? 'Sort by Total Orders' : 'Sort by Marketplace'}
                    />
                </div>
            </div>
            <div style={{margin: '10px'}}>
                <svg ref={svgRef} width={width} height={height}>
                    <g className="x-axis"/>
                    <g className="y-axis"/>
                </svg>
            </div>
        </div>
    )
}

export default MarketplaceBarChart;
