document.addEventListener('keydown', function(event) {
    let keycode = event.which;
    for(let key of document.querySelectorAll(".key")){
        if(key.getAttribute("keycode") == keycode){
            key.classList.add("darker");
            updateKey(key)
        }
    }
})

document.addEventListener('keyup', function(event){
    let keycode = event.which;
    for(let key of document.querySelectorAll(".key")){
        if(key.getAttribute("keycode") == keycode){
            key.classList.remove("darker");
        }
    }
})

function closePopup() {
    var popup = document.querySelector('.popup');
    popup.style.display = 'none';
    var popup = document.querySelector('.overlay');
    popup.style.display = 'none';
  }


  function openPopup() {
    var popup = document.querySelector('.popup');
    popup.style.display = 'block';
    var popup = document.querySelector('.overlay');
    popup.style.display = 'block';
  }


let currentkey = null;

function addbind(){
    if(currentkey != null){

    }
}

function updateKey(key){
    const blist = document.getElementById("binds-list");

    document.getElementById("keyname").innerHTML = key.innerHTML;

    blist.innerHTML = "";

    let binds = key.getAttribute("binds").split(":");
    console.log(binds);

    for(let bind of binds){
        let bind_element = document.createElement("h3");
        bind_element.classList.add("bind-indicator");
        bind_element.innerHTML = bind;
        blist.appendChild(bind_element);
    }

    currentkey = key;
}

