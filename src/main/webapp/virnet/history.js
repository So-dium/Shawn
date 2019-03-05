/**
 * change url hash and add event echo  
 */
//这个写法无效
//window.addEventListener('popstate', function(e){
//	
//  alert("aaaaa");
//  if (history.state){
//    var state = history.state;
//    //do something(state.url, state.title);
//    switch(state.funName){
//       case 'fetchData' : var data = state.data; fetchData(data['id'], data['user'], data['page'], data['select']);
//    }
//  }
//},false);

window.onhashchange=function(e){
	
  if (history.state){
    var state = history.state;
    //do something(state.url, state.title);
    switch(state.funName){
       case 'fetchData' : var data = state.data; 
                          fetchData(data['id'], data['user'], data['page'], data['select'],true);
                          break;
       case 'fetchFacilitiesData' : var data = state.data;
                                    fetchFacilitiesData(data['id'], data['user'], data['page'], data['select'],
                                    		            data['physicsMachinesName'],true);
                                    break;
       default : window.history.back();break;
    }
  }
};

function storeHistory(hash, funName, data){
	url = window.location.href;
	Url = url.split('#');
	
	var state = {
		    title : "",
		    url : Url[0] + "#" + hash,
		    funName : funName,
		    data : data
		};
	window.history.pushState(state, document.title, Url[0] + "#" + hash);
}