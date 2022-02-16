



var sku_list;  //array of sku dict
var graph_data;

// var dropdown_marketplace = 'US';
var dropdown_marketplace_options;
var selected_marketplace;

var dropdown_kcode_options;
var selected_kcode;

var dropdown_sku_options;
var selected_sku;


var ads_toggle = true;
var review_toggle = true;
var milestone_toggle = true;

// fake data
var sku_list_json = '{"list_of_sku":[{"marketplace": "US","k_code": "US-K510","sku_code": "US-K510-R1"},{"marketplace": "US","k_code": "US-K510","sku_code": "US-K510-R2"},{"marketplace": "US","k_code": "US-K510","sku_code": "US-K510-R3"},{"marketplace": "US","k_code": "US-K512","sku_code": "US-K512-85U20001R0"},{"marketplace": "US","k_code": "US-K513","sku_code": "US-K513-85U20001R0"},{"marketplace": "UK","k_code": "UK-K510","sku_code": "UK-K510-85U20001R0"},{"marketplace": "UK","k_code": "UK-K512","sku_code": "UK-K512-85U20001R0"},{"marketplace": "UK","k_code": "UK-K513","sku_code": "UK-K513-85U20001R0"},{"marketplace": "CA","k_code": "CA-K510","sku_code": "CA-K510-R1"},{"marketplace": "CA","k_code": "CA-K510","sku_code": "CA-K510-R2"},{"marketplace": "CA","k_code": "CA-K510","sku_code": "CA-K510-R3"},{"marketplace": "CA","k_code": "CA-K512","sku_code": "CA-K512-85U20001R0"},{"marketplace": "CA","k_code": "CA-K513","sku_code": "CA-K513-85U20001R0"}]}'

var SKU1_json = '{"k_code":"K510","sku_code":"K10-85U20001R0","marketplace":"US","data":[{"sales_data":[{"date":"2018-01-02","sales":1392},{"date":"2018-01-10","sales":1820},{"date":"2018-01-13","sales":2382},{"date":"2018-01-14","sales":3293},{"date":"2018-01-15","sales":1233},{"date":"2018-02-05","sales":3992},{"date":"2018-02-15","sales":3928},{"date":"2018-02-23","sales":3891},{"date":"2018-03-02","sales":2918},{"date":"2018-03-13","sales":4138},{"date":"2018-03-29","sales":5382},{"date":"2018-04-02","sales":2817},{"date":"2018-04-18","sales":3182},{"date":"2018-04-23","sales":3928},{"date":"2018-05-08","sales":5219},{"date":"2018-05-12","sales":1928},{"date":"2018-05-23","sales":4919}]},{"activity_data":[{"activity_type":"ads","date":"2018-01-10","activity_name":"Facebook ads starts"},{"activity_type":"ads","date":"2018-05-08","activity_name":"Reddit ads start"},{"activity_type":"review","date":"2018-01-14","activity_name":"Online media review"},{"activity_type":"review","date":"2018-03-29","activity_name":"Early reviewer program"},{"activity_type":"milestone","date":"2018-02-15","activity_name":"Receive first amazon review"},{"activity_type":"milestone","date":"2018-05-12","activity_name":"Receive three 5 stars reviews on amazon"}]}]}';
var SKU2_json = '{"k_code":"K510","sku_code":"K10-85U20001R0","marketplace":"US","data":[{"sales_data":[{"date":"2018-01-04","sales":1391},{"date":"2018-01-08","sales":1820},{"date":"2018-01-13","sales":2332},{"date":"2018-01-19","sales":4293},{"date":"2018-01-23","sales":1333},{"date":"2018-02-01","sales":2392},{"date":"2018-02-14","sales":8990},{"date":"2018-02-23","sales":3891},{"date":"2018-03-02","sales":2128},{"date":"2018-03-13","sales":4132},{"date":"2018-03-23","sales":5382},{"date":"2018-04-01","sales":2890},{"date":"2018-04-18","sales":3132},{"date":"2018-04-23","sales":4928},{"date":"2018-05-08","sales":8219},{"date":"2018-05-10","sales":2328},{"date":"2018-05-30","sales":4129}]},{"activity_data":[{"activity_type":"ads","date":"2018-04-23","activity_name":"Facebook ads start"},{"activity_type":"ads","date":"2018-05-30","activity_name":"Reddit ads start"},{"activity_type":"review","date":"2018-03-02","activity_name":"Online media review"},{"activity_type":"review","date":"2018-01-19","activity_name":"Early reviewer program"},{"activity_type":"milestone","date":"2018-02-23","activity_name":"Receive first amazon review"},{"activity_type":"milestone","date":"2018-01-13","activity_name":"Receive three 5 stars reviews on amazon"}]}]}';
var SKU3_json = '{"k_code":"K510","sku_code":"K10-85U20001R0","marketplace":"US","data":[{"sales_data":[{"date":"2018-01-04","sales":1331},{"date":"2018-01-06","sales":9820},{"date":"2018-01-13","sales":2332},{"date":"2018-01-19","sales":2293},{"date":"2018-01-23","sales":1233},{"date":"2018-02-03","sales":2092},{"date":"2018-02-14","sales":8990},{"date":"2018-02-29","sales":3891},{"date":"2018-03-07","sales":2128},{"date":"2018-03-13","sales":6132},{"date":"2018-03-29","sales":5382},{"date":"2018-04-01","sales":9830},{"date":"2018-04-13","sales":3132},{"date":"2018-04-22","sales":4028},{"date":"2018-05-08","sales":2219},{"date":"2018-05-13","sales":2228},{"date":"2018-05-30","sales":6129},{"date":"2018-06-02","sales":8219},{"date":"2018-06-13","sales":2821},{"date":"2018-06-21","sales":4129}]},{"activity_data":[{"activity_type":"ads","date":"2018-02-29","activity_name":"Facebook ads start"},{"activity_type":"ads","date":"2018-03-29","activity_name":"Reddit ads start"},{"activity_type":"review","date":"2018-04-22","activity_name":"Online media review"},{"activity_type":"review","date":"2018-06-21","activity_name":"Early reviewer program"},{"activity_type":"milestone","date":"2018-02-14","activity_name":"Receive first amazon review"},{"activity_type":"milestone","date":"2018-01-04","activity_name":"Receive three 5 stars reviews on amazon"}]}]}';
