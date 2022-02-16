

function set_all_toggles_true(){

	// set toggles to true
	ads_toggle = true;
	$('#ads_toggle').css("background-color", "rgb(102, 106, 115)");
	$('#ads_toggle').css("color", "white");

	review_toggle = true;
	$('#review_toggle').css("background-color", "rgb(102, 106, 115)");
	$('#review_toggle').css("color", "white");

	milestone_toggle = true;
	$('#milestone_toggle').css("background-color", "rgb(102, 106, 115)");
	$('#milestone_toggle').css("color", "white");

};


function register_click_listeners(){

	
	// category fade in and fade out
	$('.category_btn').click(function(){

		// console.log($(this).attr('id'));

		if($(this).attr('id') == 'ads_toggle'){

			// ads_toggle to false
			if(ads_toggle == true){
				ads_toggle = false;
				$(this).css("background-color", "white");
				$(this).css("color", "black");

				$(".ads_type").fadeOut();

			}else{
				ads_toggle = true;
				$(this).css("background-color", "rgb(102, 106, 115)");
				$(this).css("color", "white");
				$(".ads_type").fadeIn();
			}
		}

		if($(this).attr('id') == 'review_toggle'){
			if(review_toggle == true){
				review_toggle = false;
				$(this).css("background-color", "white");
				$(this).css("color", "black");

				$(".review_type").fadeOut();

			}else{
				review_toggle = true;
				$(this).css("background-color", "rgb(102, 106, 115)");
				$(this).css("color", "white");

				$(".review_type").fadeIn();

			}
		}
		
		if($(this).attr('id') == 'milestone_toggle'){
			if(milestone_toggle == true){
				milestone_toggle = false;
				$(this).css("background-color", "white");
				$(this).css("color", "black");

				$(".milestone_type").fadeOut();

			}else{
				milestone_toggle = true;
				$(this).css("background-color", "rgb(102, 106, 115)");
				$(this).css("color", "white");

				$(".milestone_type").fadeIn();
			}
		}
	})

}


