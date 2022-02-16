
import React, { useState,useEffect, useRef } from "react";
import Select from 'react-select';
import {
  select,
  scaleBand,
  axisBottom,
  stack,
  max,
  scaleLinear,
  scaleOrdinal,
  axisLeft,
  stackOrderAscending
} from "d3";
import { tip as d3tip } from "d3-v6-tip";

// test data
// const usData = [{bpCode: "k612-A", inBound: 12, inStock: 1, transfer: 13},
//   {bpCode: "k612-B", inBound: 6, inStock: 6, transfer: 33},
//   {bpCode: "k612-C", inBound: 11, inStock: 28, transfer: 12},
//   {bpCode: "k612-D", inBound: 19, inStock: 6, transfer: 1}]

// const deData = [{bpCode: "k612-A", inBound: 9, inStock: 7, transfer: 10},
//   {bpCode: "k612-B", inBound: 11, inStock: 22, transfer: 19},
//   {bpCode: "k612-C", inBound: 8, inStock: 8, transfer: 2}];


const InventoryStackedBarChart = ({mp, mpOp, onSelect, data}) => {
    const svgRef = useRef()
    const [width, setWidth] = useState(1200)
    const [height, setHeight] = useState(300)
    const [colorSet, setColor] = useState(['#094173', '#134ea8', '#408cf5'])
    const [subgroups, setSubGroups] = useState(['inBound', 'inStock', 'transfer'])

    const margin = {top: 10, right: 30, bottom: 100, left: 50},
        w = width - margin.left - margin.right,
        h = height - margin.top - margin.bottom;
    
    const color = scaleOrdinal()
        .domain(subgroups)
        // .range(['#053763', '#0052cc', '#1976f5'])
        .range(colorSet)
        // .range(['#e41a1c','#377eb8','#4daf4a'])

    const xScale = scaleBand()
        .domain(data.map(d => d.bpCode))
        .range([0, w])
        .paddingInner(0.5)

    const yScale = scaleLinear()
        .domain([0, max(data, d => d.inBound + d.inStock + d.transfer)])
        .range([ h, 0 ]);
    
    let tip = d3tip()
        .attr('class', 'd3-tip')
        .offset([-10, 0])
        .html((e,d) => {
            const target = e.target.parentNode.className.baseVal;
            return "<span>" + d.data.sku + "</br>" + target + ":" + d.data[target] + "</span>";
        })
    
    const handleMouseEnter = (e, d) => {
        tip.show(e, d)
        select(e.target).attr('opacity', '0.9')
    }
    const handleMouseLeave = (e, d) => {
        tip.hide(e,d)
        select(e.taret).attr('opacity', '1')
    }

    const renderChart = () => {
        const svg = select(svgRef.current);        
        const group = svg.append('g')
            .attr("class", 'stack-group')
            .attr('transform', `translate(${margin.left},${margin.top} )`)        

        const xAxis = axisBottom(xScale).tickSizeOuter(0);
        svg.select(".x-axis")
            .attr("transform",`translate(${margin.left}, ${h + margin.top + 5})`)
            .call(xAxis)
            .selectAll("text")
            .attr("transform", "translate(-10,0)rotate(-45)")
            .style("text-anchor", "end");
    
        const yAxis = axisLeft(yScale)
        svg.select(".y-axis")
            .attr("transform", `translate(${margin.left - 5}, ${margin.top})`)
            .call(yAxis);

        //stack the data? --> stack per subgroup
        const stackedData = stack().keys(subgroups)(data)
    
        // Show the bars
        group.selectAll("g")
            // Enter in the stack data = loop key per key = group per group
            .data(stackedData)
            .join("g")
                .attr("fill", d => color(d.key))
                .attr("class", d => d.key)
                .selectAll("rect")
                // enter a second time = loop subgroup per subgroup to add all rectangles
                .data(d => d)
                .join("rect")
                .attr("x", d => xScale(d.data.sku))
                .attr("y", d => yScale(d[1]))
                .attr("height", d => yScale(d[0]) - yScale(d[1]))
                .attr("width",xScale.bandwidth())
        const groups = group.selectAll('g').data(stackedData)
        groups.selectAll("rect")
            .on("mouseenter", (e, d) => {
                handleMouseEnter(e, d)
            })
            .on("mouseleave", (e, d)=> {
                handleMouseLeave(e, d)
            })
        svg.call(tip)
    }

    const updateRender = () => {
        // console.log('update')
        const svg = select(svgRef.current);
        const group = svg.select('.stack-group')
        const stackedData = stack().keys(subgroups)(data)

        const update = group.selectAll('g').data(stackedData)
        const updatexAxis = svg.select('.x-axis');
        const updateyAxis = svg.select('.y-axis');

        xScale.domain(data.map(d => d.sku))
        yScale.domain([0, max(data, d => d.inBound + d.inStock + d.transfer)])

        update
            .join(
                enter => enter
                    .append("g")
                    .attr("fill", d => color(d.key))
                    .attr("class", d => d.key),
                null, // no update function
                exit => {
                    exit
                        .transition().duration(750)
                        .style("fill-opacity", 0)
                        .remove();
                }
            ).selectAll("rect")
                .data(d => d)
                    .join(
                        enter => enter
                            .append("rect")
                            .attr("class", "bar")
                            .attr("x", d => {
                                // console.log(d.data)
                                return xScale(d.data.sku);
                            })
                            .attr("y", () => {
                            return yScale(0);
                            })
                            .attr("height", () => {
                            return h - yScale(0);
                            })
                            .attr("width", xScale.bandwidth())
                        ,
                        null,
                        exit => {
                            exit
                                .transition().duration(750)
                                .style("fill-opacity", 0)
                                .remove();
                        }
                    )
                    .transition().duration(750)
                    // .delay((d, i) => i * 20)
                    .attr("x", d => xScale(d.data.sku))
                    .attr("y", d => {
                        return yScale(d[1]);
                    })
                    .attr("width", xScale.bandwidth())
                    .attr("height", d => {
                        return yScale(d[0]) - yScale(d[1]);
                    });
        update.selectAll('rect')
            .on('mouseenter', (e, d) => {
                handleMouseEnter(e, d)
            })
            .on('mouseleave', (e, d)=> {
                handleMouseLeave(e, d)
            })
        svg.call(tip)

        const xAxis = axisBottom(xScale).tickSizeOuter(0);
        updatexAxis.enter()
            .merge(updatexAxis)
            .attr("transform",`translate(${margin.left}, ${h + margin.top + 5})`)
            .call(xAxis)
            .selectAll("text")
            .attr("transform", "translate(-10,0)rotate(-45)")
            .style("text-anchor", "end");

        const yAxis = axisLeft(yScale)
        updateyAxis.enter()
            .merge(updateyAxis)
            .attr("transform", `translate(${margin.left - 5}, ${margin.top})`)
            .call(yAxis);
        
        updatexAxis.exit()
            .transition().duration(750).remove()

        updateyAxis.exit()
            .transition().duration(750).remove()
    }

    const onSelectMp = (e) => {
        onSelect(e.value)
    }

    const getWidth = () => {
       const sw = document.getElementById('skuorderbar').offsetWidth
       const mw = document.getElementById('mpbar').offsetWidth
       const totalWidth = sw + mw ;
       setWidth(totalWidth)
    }
    
    useEffect(() => {
        renderChart() //initially render chart
        getWidth() //initially set width of svg
        window.addEventListener("resize", getWidth); //instantly calculate width
    },[])

    useEffect(() => {
        if (data.length > 0) updateRender() // rerender chart when data changes
    },[data])


    return (
        <div>
            <div style={{display: 'flex', alignItems: 'center', justifyContent: 'space-between'}}>
                <p className="data-card-title">SKU 庫存</p>
                <div className="data-card-control">
                    {/* <FormControlLabel
                        control={
                            <BlueSwitch size="small" checked={isSorted} onChange={toggleChecked} />
                        }
                        label={isSorted ? 'Sort by Orders' : 'Sort by SKU'}
                    /> */}
                    <Select
                        className='drs-selector'
                        placeholder={mp}
                        options={mpOp}
                        value={mp}
                        onChange={(e) => onSelectMp(e)}
                    />
                </div>
            </div>
            <div style={{margin: '10px'}}>
                <ul className="dashboard-legend">
                    <li className="legend-line-item">
                        <span className="dot" style={{backgroundColor: `${colorSet[0]}`}}></span>
                        <span style={{marginLeft: '5px'}}>{subgroups[0]}</span>
                    </li>
                    <li className="legend-line-item">
                        <span className="dot" style={{backgroundColor: `${colorSet[1]}`}}></span>
                        <span style={{marginLeft: '5px'}}>{subgroups[1]}</span>
                    </li>
                    <li className="legend-line-item">
                        <span className="dot" style={{backgroundColor: `${colorSet[2]}`}}></span>
                        <span style={{marginLeft: '5px'}}>{subgroups[2]}</span>
                    </li>
                </ul>
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


export default InventoryStackedBarChart;