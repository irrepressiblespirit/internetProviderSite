/**
 * 
 */
    function showError(container, errorMessage) {
      container.className = 'error';
      var msgElem = document.createElement('span');
      msgElem.className = "error-message";
      msgElem.innerHTML = errorMessage;
      container.appendChild(msgElem);
    }

    function resetError(container) {
      container.className = '';
      if (container.lastChild.className == "error-message") {
        container.removeChild(container.lastChild);
      }
    }

    function validate(form) {
    	 var elems = form.elements;
    	 resetError(elems.log.parentNode);
    	 if (!elems.log.value) {
    	        showError(elems.log.parentNode, ' Wrong login.');
    	        event.preventDefault();
    	 }
    	 resetError(elems.pass.parentNode);
    	 if(!elems.pass.value){
    		 showError(elems.pass.parentNode, ' Wrong password.');
    		 event.preventDefault();
    	 }
    	 resetError(elems.firstName.parentNode);
    	 if(!elems.firstName.value){
    		 showError(elems.firstName.parentNode, ' Wrong first name.');
    		 event.preventDefault();
    	 }else{
    		 var str=elems.firstName.value;
    		 if(str.search(/[a-z]+/)==-1){
    			 showError(elems.firstName.parentNode, ' Wrong first name.');
    			 event.preventDefault();
    		 }
    	 }
    	 resetError(elems.lastName.parentNode);
    	 if(!elems.lastName.value){
    		 showError(elems.lastName.parentNode, ' Wrong last name.');
    		 event.preventDefault();
    	 }else{
    		 var str=elems.lastName.value;
    		 if(str.search(/[a-zA-Z[а-яА-Я]]+/)==-1){
    			 showError(elems.lastName.parentNode, ' Wrong last name.');
    			 event.preventDefault();
    		 }
    	 }
    	 resetError(elems.tel.parentNode);
    	 if(!elems.tel.value){
    		 showError(elems.tel.parentNode, ' Wrong telephone number.');
    		 event.preventDefault();
    	 }else{
    		 var str=elems.tel.value;
    		 if(str.match(/[0-9]{10}/)==null){
    			 showError(elems.tel.parentNode, ' Wrong telephone number.');
    			 event.preventDefault();
    		 }
    	 }
    	 resetError(elems.address.parentNode);
    	 if (!elems.address.value) {
 	        showError(elems.address.parentNode, ' Wrong address.');
 	        event.preventDefault();
 	     }
    	 resetError(elems.email.parentNode);
    	 if(!elems.email.value){
    		 showError(elems.email.parentNode, ' Wrong email.');
    		 event.preventDefault();
    	 }else{
    		 var str=elems.email.value;
    		 if(str.search(/[a-zA-Z]+@[a-z]+\\.[a-z]+/)==-1){
    			 showError(elems.email.parentNode, ' Wrong email.');
    			 event.preventDefault();
    		 }
    	 }
    	 resetError(elems.balance.parentNode);
    	 if(!elems.balance.value){
    		 showError(elems.balance.parentNode, ' Wrong balance.');
    		 event.preventDefault();
    	 }else{
    		 var str=elems.balance.value;
    		 if(str.match(/[0-9]+/)==null){
    			 showError(elems.balance.parentNode, ' Wrong balance.');
    			 event.preventDefault();
    		 }
    	 }
    }