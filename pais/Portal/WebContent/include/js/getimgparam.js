function callback() {
	if (xmlhttp.readyState == 4) {
		if (xmlhttp.status == 200) {
			result = xmlhttp.responseText;
			//alert("we made it");
		} else {
			//alert(" An error has occurred: " + xmlhttp.statusText);
		}

	}
}
