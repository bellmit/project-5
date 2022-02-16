

function draw_graph(){

  var sales_data = graph_data.salesData;
  var activity_data = graph_data.activityData;
  var no_data_text = document.getElementById('no_data_text');

  
  if(sales_data.length > 0){
	  
	  // hide no data text
	  no_data_text.style.display = "";

	  var axis_stroke_width = '1.2px';
	  var axis_font = '12px arial';
	
	  var ads_color = '#4caf50';
	  var review_color = '#2196f3';
	  var milestone_color = '#ff5252';
	
	 
	  // draw chart function
	  function transition(path) {
	      path.transition()
	          .duration(3000)
	          .attrTween("stroke-dasharray", tweenDash);
	  }
	  function tweenDash() {
	      var l = this.getTotalLength(),
	          i = d3.interpolateString("0," + l, l + "," + l);
	      return function (t) { return i(t); };
	  }
	
	
	  // set dimension for graph
	  var margin = {top:20, right: 60, bottom: 100, left: 60},
	    // width = 800 - margin.left - margin.right,
	    width = $('#line_graph').width() - margin.left - margin.right,
	    height = 500 - margin.top - margin.bottom;
	
	
	  // parse datetime
	  var parseTime = d3.timeParse("%Y-%m-%d");
	  var formatTime = d3.timeFormat("%Y-%m-%d");
	
	  // setting x, y range
	  // range is what entering each data returns
	  // enter domain => result range
	  var x = d3.scaleTime().range([0, width]);
	  var y = d3.scaleLinear().range([height, 0]);
	
	  // define the line
	  var valueline = d3.line()
	      .x(function(d){
	        return x(d.date);
	      })
	      .y(function(d){
	        return y(d.sales);
	      })
	
	  // append a svg object to the body of the page
	  // appends a 'group' element to 'svg'
	  // moves the 'group' element to the top left margin
	  var svg = d3.select('#line_graph').append('svg')
	    .attr('width', width + margin.left + margin.right)
	    .attr('height', height + margin.top + margin.bottom)
	    .append('g')
	    .attr('transform', 'translate(' + margin.left + ',' + margin.top + ')');
	

	    // format the data
	    sales_data.forEach(function(d){
	      // d.date = d.date;
	      d.date = parseTime(d.date);
	      d.sales = +d.sales;
	    });
	
	    // console.log(sales_data)
	
	    activity_data.forEach(function(d){
	      d.activityType = d.activityType;
	      // d.date = d.date;
	      d.date = parseTime(d.date);
	      d.activityName = d.activityName;
	
	    })
	
	
	    // scales the range of the data
	    // set domain for the "graph"
	    x.domain(d3.extent(sales_data, function(d){ return d.date; }));
	    y.domain([0, d3.max(sales_data, function(d){ return d.sales; })*1.2 ]);
	
	  
	    // add the valueline path
	    svg.append('path')
	        .data([sales_data])
	        .attr('class', 'line')
	        .attr('d', valueline)
	        .call(transition);
	
	
	    // Add the X Axis
	    svg.append("g")
	        .attr("class", "axis")
	        .style("font", axis_font)
	        .style("stroke-width", axis_stroke_width)
	        .attr("transform", "translate(0," + height + ")")
	        .call(d3.axisBottom(x)
	        .tickFormat(d3.timeFormat("%Y-%m-%d")))
	        .selectAll("text")  
	        .style("text-anchor", "end")
	        .attr("dx", "-.8em")
	        .attr("dy", ".15em")
	        .attr("transform", "rotate(-65)");
	
	
	    // Add the Y Axis
	    svg.append("g")
	        .attr("class", "axis")
	        .style("font", axis_font)
	        .style("stroke-width", axis_stroke_width)
	        .call(d3.axisLeft(y));
	
	        // #ffab00
	    var sales_point_circles = svg.selectAll('.sales_point_circle')
	      .data(sales_data)
	      .enter()
	      .append('circle')
	      .attr('class', 'dot')
	      .attr('cx', function(d, i){
	         return x(d.date);
	      })
	      .attr('cy', function(d){
	          return y(d.sales);
	      })
	      .attr('r', 0);
	
	    sales_point_circles.transition()
	      .duration(3000)
	      .attr('r', 1);
	
	
	    var g = svg.append('g')
	    			.attr('class', 'everything');
	    
	    var nodes = g.append('g')
	    				.attr('class', 'nodes');
	    
	    var node = nodes.selectAll('node')
	    				.data(activity_data)
	    				.enter()
	    				.append('g')
	    				.attr('class', 'node');
	    						
	    
	    var description_box_lines = node.append('line')
	      .attr("x1", function(d, i){
	         return x(d.date);
	      })  
	      .attr("y1", function(d){
	    	  
	    	var matched = false;
			  
			// search for corresponding y value
			for(i=0; i<sales_data.length; i++){
			
				if(formatTime(d.date) == formatTime(sales_data[i].date)){
					matched = true;	
					// console.log(y(sales_data[i].sales)-110);
					return y(sales_data[i].sales)-45;
				}
			
				if(i == sales_data.length -1 && matched == false){
					// console.log('no match');
					//set to same as 0 sales
					return 335;
				}
			    	            
			}
	        
	        
	      })
	      .attr("x2", function(d, i){
	         return x(d.date);
	      }) 
	      .attr("y2", function(d){
	    	  
	    	  var matched = false;
			  
				// search for corresponding y value
				for(i=0; i<sales_data.length; i++){
				
					if(formatTime(d.date) == formatTime(sales_data[i].date)){
						matched = true;	
						return y(sales_data[i].sales);
					}
				
					if(i == sales_data.length -1 && matched == false){
						//set to same as 0 sales
						return 380;
					}
				    	            
				}
	    	  
	      })
	      .attr('class', function(d){
	        if(d.activityType == 'ads'){
	          return 'ads_type';
	        }else if(d.activityType == 'review'){
	          return 'review_type';
	        }else if(d.activityType == 'milestone'){
	          return 'milestone_type';
	        }
	      })
	      .style("stroke-width", 0)
	      .style("position", "absolute")
	      .style("stroke", function(d){
	        if(d.activityType == 'ads'){
	          return ads_color;
	        }else if(d.activityType == 'review'){
	          return review_color;
	        }else if(d.activityType == 'milestone'){
	          return milestone_color;
	        }
	      });
	
	      
	    description_box_lines.transition()
	      .duration(3000)
	      .style("stroke-width", 2);
	
	
	    var description_boxes = node.append('rect')
	      .attr("x", function(d, i){
	         return x(d.date)-55;
	      })  
	      .attr("y", function(d){
	    	  
	    	  var matched = false;
	    	  
	          // search for corresponding y value
	          for(i=0; i<sales_data.length; i++){

				if(formatTime(d.date) == formatTime(sales_data[i].date)){
				
					matched = true;	
					
					return y(sales_data[i].sales)-110;
				}
				
				if(i == sales_data.length -1 && matched == false){
					//set to same as 0 sales
					return 270;
				}
	            	            
	          }
	         
	      })
	      .attr('class', function(d){
	        if(d.activityType == 'ads'){
	          return 'ads_type';
	        }else if(d.activityType == 'review'){
	          return 'review_type';
	        }else if(d.activityType == 'milestone'){
	          return 'milestone_type';
	        }
	      })
	      .attr("width", 110)
	      .attr("height", 0)
	      .attr("rx", 2)
	      .attr("ry", 2)
	      .style("fill", function(d){
	        if(d.activityType == 'ads'){
	          return '#e8f5e9';
	        }else if(d.activityType == 'review'){
	          return '#e3f2fd';
	        }else if(d.activityType == 'milestone'){
	          return '#ffebee';
	        }
	      })
	      .style("stroke", function(d){
	        if(d.activityType == 'ads'){
	          return ads_color;
	        }else if(d.activityType == 'review'){
	          return review_color;
	        }else if(d.activityType == 'milestone'){
	          return milestone_color;
	        }
	      });
	      
	      
	      
	
	    description_boxes.transition()
	      .duration(3500)
	      .attr("height", 75);
	
	    
	
	    var event_description_texts = node.append("text")
	       .text(function(d) {
	          return d.activityName;
	       })
	       .attr("x", function(d, i){
	           return x(d.date)-50;
	        })  
	        .attr("y", function(d){

				var matched = false;
					  
				// search for corresponding y value
				for(i=0; i<sales_data.length; i++){
				
					if(formatTime(d.date) == formatTime(sales_data[i].date)){
						matched = true;	
						return y(sales_data[i].sales)-90;
					}
				
					if(i == sales_data.length -1 && matched == false){
						// console.log('no match');
						//set to same as 0 sales
						return 285;
					}
				    	            
				}
	            
	        })
	        .attr('class', function(d){
	          if(d.activityType == 'ads'){
	            return 'ads_type';
	          }else if(d.activityType == 'review'){
	            return 'review_type';
	          }else if(d.activityType == 'milestone'){
	            return 'milestone_type';
	          }
	        })
	        .attr("font-family", "sans-serif")
	        .attr("font-size", "14px")
	        .style("opacity", 0)
	        .style("background-color", "red")
	        .call(wrap, 100);
	
	
	    event_description_texts.transition()
	      .duration(20000)
	      .style("opacity", 100);
	      
	      
	    d3.selectAll('.node').on('mouseenter', function(){
	    	this.parentElement.appendChild(this);
	    });  
	      
  }else{
	  
	  no_data_text.style.display = "inline";
	  
  }      
	      
};



// text wrap for description
function wrap(text, width) {
    text.each(function () {
        var text = d3.select(this),
            words = text.text().split(/\s+/).reverse(),
            word,
            line = [],
            lineNumber = 0,
            lineHeight = 1.1, // ems
            x = text.attr("x"),
            y = text.attr("y"),
            dy = 0, //parseFloat(text.attr("dy")),
            tspan = text.text(null)
                        .append("tspan")
                        .attr("x", x)
                        .attr("y", y)
                        .attr("dy", dy + "em");
        while (word = words.pop()) {
            line.push(word);
            tspan.text(line.join(" "));
            if (tspan.node().getComputedTextLength() > width) {
                line.pop();
                tspan.text(line.join(" "));
                line = [word];
                tspan = text.append("tspan")
                            .attr("x", x)
                            .attr("y", y)
                            .attr("dy", ++lineNumber * lineHeight + dy + "em")
                            .text(word);
            }
        }
    });
}




