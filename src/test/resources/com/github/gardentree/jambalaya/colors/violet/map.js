var wand = {};
(function(){
	wand.getDataFromHash = function() {
	  var data = location.hash.slice(1).split(',');
	  print(data);
	  if (data.length == 3) {
	    var lat = parseFloat(data[0]);
	    var lng = parseFloat(data[1]);
	    var zoom = parseInt(data[2],10);
	    return {lat: lat, lng: lng, zoom: zoom};
	  }
	}
})();
