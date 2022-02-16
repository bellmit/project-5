import React, {useState, useEffect, useRef} from 'react';
import Select from 'react-select';
import { scaleLinear, scaleBand, select, axisBottom, axisLeft, max, format, descending, ascending} from 'd3';
import axios from 'axios';
import {DOMAIN_NAME} from '../../constants/action-types';
import { withStyles } from '@material-ui/core/styles';
import { blue } from '@material-ui/core/colors';
import Switch from '@material-ui/core/Switch';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import { tip as d3tip } from "d3-v6-tip";

const Url = DOMAIN_NAME + '/pd';

// testing
// const deData = [
//     {marketplace: "Amazon.de", bpCode: "BP-K612-8BSMAP0118D0700", sku: "K612-1A1", totalOrder: 4},
//     {marketplace: "Amazon.de", bpCode: "BP-K612-8BSMAP0118D0700", sku: "K612-2A1", totalOrder: 3},
//     {marketplace: "Amazon.de", bpCode: "BP-K612-8BSMAP0118D0700", sku: "K612-3A3", totalOrder: 2},
//     {marketplace: "Amazon.de", bpCode: "BP-K612-8BSMAP0118D0700", sku: "K612-4A2", totalOrder: 1},
//     {marketplace: "Amazon.de", bpCode: "BP-K612-8BSMAP0118D0700", sku: "K612-6A1", totalOrder: 1},
//     {marketplace: "Amazon.de", bpCode: "BP-K612-8BSMAP0118D0700", sku: "K612-7A2", totalOrder: 1},
//     {marketplace: "Amazon.de", bpCode: "BP-K612-8BSMAP0118D0700", sku: "K612-8A1", totalOrder: 1},
//     {marketplace: "Amazon.de", bpCode: "BP-K612-8BSMAP0118D0700", sku: "K612-9A2", totalOrder: 3},
//     {marketplace: "Amazon.de", bpCode: "BP-K612-8BSMAP0118D0700", sku: "K612-AA1", totalOrder: 1},
//     {marketplace: "Amazon.de", bpCode: "BP-K612-8BSMAP0118D0700", sku: "K612-AA3", totalOrder: 1},
// ]

// const usData = [
//     {marketplace: "Amazon.com", bpCode: "BP-K612-8BSMAP0118D0700", sku: "K612-1A1", totalOrder: 29},
//     {marketplace: "Amazon.com", bpCode: "BP-K612-8BSMAP0118D0700", sku: "K612-2A1", totalOrder: 30},
//     {marketplace: "Amazon.com", bpCode: "BP-K612-8BSMAP0118D0700", sku: "K612-6A1", totalOrder: 4},
//     {marketplace: "Amazon.com", bpCode: "BP-K612-8BSMAP0118D0700", sku: "K612-7A2", totalOrder: 5},
//     {marketplace: "Amazon.com", bpCode: "BP-K612-8BSMAP0118D0700", sku: "K612-8A1", totalOrder: 7},
//     {marketplace: "Amazon.com", bpCode: "BP-K612-8BSMAP0118D0700", sku: "K612-9A2", totalOrder: 10},
//     {marketplace: "Amazon.com", bpCode: "BP-K612-8BSMAP0118D0700", sku: "K612-AA1", totalOrder: 12},
//     {marketplace: "Amazon.com", bpCode: "BP-K612-8BSMAP0118D0700", sku: "K612-AA3", totalOrder: 3},
// ]

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

const SkuOrdersBarChart = ({mp,mpOp,onSelect,data, onSortData}) => {

    const svgRef = useRef();
    const [width, setWidth] = useState(660)
    const [height, setHeight] = useState(360)
    const [isSorted, setIsSorted] = useState(false)

    const margin = {left: 100, right: 10, top: 30, bottom: 100}
    const w = width - margin.left -margin.right;
    const h = height - margin.top - margin.bottom;

    const xPadding = (data) => {
        if (data.length > 9) { 
            return 0.05 
        } else if (data.length < 5) {
            return 0.9
        } else {
            return 0.6
        }
    }
    let xScale = scaleBand()
        .domain(data.map(function(d) { return d.sku; }))
        .rangeRound([0, w])
        .paddingInner(xPadding(data));
    
    let yScale = scaleLinear()
        .domain([0, max(data, d => d.totalOrder)])
        .rangeRound([h,0]);

    let colorScale = scaleLinear()
        .domain([0, data.length])
        .range(['rgba(19, 78, 168, 1)', 'rgba(19, 78, 168, 0.2)'])
        // .range(['rgba(9,65,115, 1)', 'rgba(64,140,245,1)'])
        // .range(['rgba(13, 133, 252, 1)', 'rgba(13, 133, 252, 0.2)'])
    
    let tip = d3tip()
        .attr('class', 'drs-d3-tip')
        .offset([-10, 0])
        .html(function(e,d) {
            return "<span>Total Order:" + d.totalOrder + "</span>";
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
            .attr("class", 'rect-group')
            .attr('transform', `translate(${margin.left},${margin.top} )`)
            .call(tip)

        group.selectAll("rect")
            .data(data)
            .enter()
            .append("rect")
            .attr("x", function(d, i) {
              return xScale(d.sku);
            })
            .attr("y", function(d) {
              return yScale(d.totalOrder);
            })
            .attr("width", xScale.bandwidth() )
            .attr("height", function(d) {
              return h - yScale(d.totalOrder);
            })
            .attr("fill", (d,i)=> colorScale(i))
            .on('mouseenter', (e,d) => {
                handleMouseEnter(e,d)
            })
            .on('mouseleave', (e,d) => {
                handleMouseLeave(e,d)
            })

            // .attr("fill", function(d) {
            //   return "rgb("+ Math.round(d.totalOrder * 4) + ",112," + Math.round(d.totalOrder+230 ) + ")";
            // });
        
        const xAxis = axisBottom(xScale);
        svg.select(".x-axis")
            .attr("transform",`translate(${margin.left}, ${h + margin.top + 5})`)
            .call(xAxis)
            .selectAll("text")
            .attr("transform", "translate(-10,0)rotate(-45)")
            .style("text-anchor", "end");
            
        const yAxis = axisLeft(yScale).tickFormat(format('d'));
        if (yScale.domain() [1] < 10) {
            yAxis.ticks(yScale.domain()[1])
        }
        svg.select('.y-axis')
            .attr("transform", `translate(${margin.left - 5}, ${margin.top})`)
            .call(yAxis);

        svg.append('text')
            .attr('class', 'y-label')
            .attr('x', -(h/ 2)- 15)
            .attr('y', 10)
            .attr('transform', 'rotate(-90)')
            .attr('text-anchor', 'middle')
            .text('Orders')
    }

    const updateBar = () => {
        const svg = select(svgRef.current);
        const group = svg.select('.rect-group')
        const update = group.selectAll('rect').data(data)
        const updatexAxis = svg.select('.x-axis');
        const updateyAxis = svg.select('.y-axis');
        
        colorScale.domain([0, data.length])
        xScale.domain(data.map(function(d) { return d.sku; }))
        yScale.domain([0, max(data, d => d.totalOrder)]) 

        update.enter()
            .append('rect')
            .attr('x', (d) => { return xScale(d.sku); })
            .attr('y', yScale(0))
            .attr('width', xScale.bandwidth())
            .attr('height', 0)
            // .attr("fill", function(d) {
            //     return "rgb("+ Math.round(d.totalOrder * 4) + ",112," + Math.round(d.totalOrder+230) + ")";
            // })
            .attr('fill', (d, i)=> colorScale(i))
            .merge(update)
            .transition().duration(1500)
            .attr("x", function(d, i) { 
                return xScale(d.sku);
              })
            .attr("y", function(d) {
                return yScale(d.totalOrder);
            })
            .attr("width", xScale.bandwidth())
            .attr("height", function(d) {
                return h - yScale(d.totalOrder);
            })

        const rects = svg.selectAll('rect')
            .on('mouseenter', (e,d) => {
               handleMouseEnter(e, d)
            })
            .on('mouseleave', (e,d) => {
               handleMouseLeave(e, d)
            })
        svg.call(tip)
        
        const xAxis = axisBottom(xScale);
        updatexAxis.enter()
            .merge(updatexAxis)
            .attr("transform",`translate(${margin.left}, ${h + margin.top + 5})`)
            .call(xAxis)
            .selectAll("text")
            .attr("transform", "translate(-10,0)rotate(-45)")
            .style("text-anchor", "end");
        
        const yAxis = axisLeft(yScale).tickFormat(format('d'));
        if (yScale.domain() [1] < 10) {
            yAxis.ticks(yScale.domain()[1])
        }
        updateyAxis.enter()
            .merge(updateyAxis)
            .attr("transform", `translate(${margin.left - 5}, ${margin.top})`)
            .call(yAxis);
    
        updatexAxis.exit()
            .transition().duration(750).remove()

        updateyAxis.exit()
            .transition().duration(750).remove()
        
        update.exit()
            .attr('fill', '#ebebeb')
            .transition().duration(750)
            .attr('y',yScale(0))
            .attr('height', 0)
            .remove();
    }

    useEffect(() => {
        // console.log('change data')
        if (data.length > 0) updateBar() // rerender bar chart when data changes
    },[data])

    useEffect(() => {
        renderChart() //initially render bar chart
    }, [])

    const onSelectMp = (e) => {
        onSelect(e.value)
    }

    const toggleChecked = () => {
        if (isSorted) {
            onSortData(false)
            setIsSorted(false)
        } else {
            onSortData(true)
            setIsSorted(true)
        }
        updateBar()
    };

    return (
        <div>
            <div style={{display: 'flex', alignItems: 'center', justifyContent: 'space-between'}}>
                <p className="data-card-title">SKU表現</p>
                <div className="data-card-control">
                    <FormControlLabel
                        control={
                            <BlueSwitch size="small" checked={isSorted} onChange={toggleChecked} />
                        }
                        label={isSorted ? 'Sort by Orders' : 'Sort by SKU'}
                    />
                    <Select
                        className='drs-selector'
                        placeholder={mp}
                        options={mpOp}
                        value={mp}
                        onChange={(e) => onSelectMp(e)}
                    />
                </div>
            </div>

            {/* testing
            <button onClick={() => newData()} className="button">change data</button> */}
            
            <div style={{margin: '10px'}}>
                <svg ref={svgRef} width={width} height={height}>
                    <g className="x-axis"/>
                    <g className="y-axis"/>
                </svg>  
            </div>
            
        </div>
    )
}

export default SkuOrdersBarChart


// testing
// const newData = () => {
//     // let changedData = data
//     // changedData = changedData.map((d,i) => Math.floor(Math.random() * 20) + 5)
//     // setData(changedData)
// }