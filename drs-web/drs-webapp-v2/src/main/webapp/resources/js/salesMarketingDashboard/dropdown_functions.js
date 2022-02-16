


function set_sku_list(sku_list_json){
	
	// parse json
	sku_list = JSON.parse(sku_list_json);
	
	// console.log(sku_list);

	return;
}



function initial_dropdown_load(){

	set_marketplace_dropdown_options();

	//select 'US' on initial
	set_marketplace_dropdown_menu('US');

	//set kcode options
	set_kcode_dropdown_options();
	// select the frist kcode
	set_kcode_dropdown_menu(dropdown_kcode_options[0]);

	//set sku options
	set_sku_dropdown_options();
	// select the frist sku
	set_sku_dropdown_menu(dropdown_sku_options[0]);

	register_dropdown_click_listener();

}



function register_dropdown_click_listener(){

	// marketplace dropdown item click
	$('.marketplace-dropdown-item').click(function(){
		
		var item_text = $(this).text();

		set_marketplace_dropdown_menu(item_text);

		//update kcode options
		set_kcode_dropdown_options();
		// select the first kcode 
		set_kcode_dropdown_menu(dropdown_kcode_options[0]);

		//set sku options
		set_sku_dropdown_options();
		set_sku_dropdown_menu(dropdown_sku_options[0]);

		register_dropdown_click_listener();

	})

	// bp dropdown item click
	$('.kcode-dropdown-item').click(function(){
		
		var item_text = $(this).text();

		// update kcode
		set_kcode_dropdown_menu(item_text);

		//set sku options
		set_sku_dropdown_options();
		set_sku_dropdown_menu(dropdown_sku_options[0]);

		register_dropdown_click_listener();

	})


	// sku dropdown item click
	$('.sku-dropdown-item').click(function(){
		
		var item_text = $(this).text();

		// update sku
		set_sku_dropdown_menu(item_text);

		register_dropdown_click_listener();

	})
}


// ===========================================================================
// marketplace dropdown

function set_marketplace_dropdown_menu(marketplace){

	//update selected_marketplace
	selected_marketplace = marketplace;
	render_marketplace_dropdown_menu();
//	console.log('Selected marketplace: ' + selected_marketplace)

}


function render_marketplace_dropdown_menu(){

	$('#marketplace_dropdown_menu_text').text(selected_marketplace);

}


function set_marketplace_dropdown_options(){

	//set dropdown_marketplace_options
	dropdown_marketplace_options = [...new Set( sku_list.map(obj => obj.marketplace)) ];
	render_marketplace_dropdown_options();

}


function render_marketplace_dropdown_options(){

	var options_html = '';

	dropdown_marketplace_options.forEach(function(option){
		// console.log(option);

		var option_html = '<a class="dropdown-item marketplace-dropdown-item">'+option+'</a>'
		options_html = options_html + option_html;

	})

	$('#marketplace_dropdown_menu_list').html(options_html)

}

// ===========================================================================
// Kcode dropdown

function set_kcode_dropdown_menu(kcode){

	//update selected_marketplace
	selected_kcode = kcode;
	render_kcode_dropdown_menu();
//	console.log('Selected kcode: ' + selected_kcode)

}


function render_kcode_dropdown_menu(){

	$('#kcode_dropdown_menu_text').text(selected_kcode);

}




function set_kcode_dropdown_options(){

	//filter kcode with marketplace
	var filtered_kcode = sku_list.filter(sku => sku.marketplace == selected_marketplace);

	//set dropdown_kcode_options with unique
	dropdown_kcode_options = [...new Set( filtered_kcode.map(obj => obj.kcode)) ];

	// render
	render_kcode_dropdown_options();
}


function render_kcode_dropdown_options(){

	var options_html = '';

	dropdown_kcode_options.forEach(function(option){
		// console.log(option);

		var option_html = '<a class="dropdown-item kcode-dropdown-item">'+option+'</a>'
		options_html = options_html + option_html;

	})

	$('#kcode_dropdown_menu_list').html(options_html)

}


// ===========================================================================
// SKU dropdown

function set_sku_dropdown_menu(sku){

	//update selected_sku
	selected_sku = sku;

	render_sku_dropdown_menu();
//	console.log('Selected sku: ' + selected_sku)

}


function render_sku_dropdown_menu(){

	$('#sku_dropdown_menu_text').text(selected_sku);

}




function set_sku_dropdown_options(){

	//filter sku with marketplace and kcode
	var filtered_sku = sku_list.filter(sku => sku.marketplace == selected_marketplace && sku.kcode == selected_kcode);


	//set dropdown_sku_options with unique sku
	// sku's are supposed to be unique after marketplace and kcode filter
	dropdown_sku_options = filtered_sku.map(obj => obj.skuCode);

	// render
	render_sku_dropdown_options();
}


function render_sku_dropdown_options(){

	var options_html = '';

	dropdown_sku_options.forEach(function(option){
		// console.log(option);

		var option_html = '<a class="dropdown-item sku-dropdown-item">'+option+'</a>'
		options_html = options_html + option_html;

	})

	$('#sku_dropdown_menu_list').html(options_html)

}


