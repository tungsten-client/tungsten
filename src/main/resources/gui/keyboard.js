document.addEventListener('keydown', function(event) {
    if(document.activeElement === document.getElementById("search-bar")) return;
    let keycode = event.which;
    for(let key of document.querySelectorAll(".key")){
        if(key.getAttribute("keycode") == keycode){
            if(event.location === KeyboardEvent.DOM_KEY_LOCATION_LEFT && key.getAttribute("location") == "l") {
                key.classList.add("darker");
                updateKey(key)
            } else if(event.location === KeyboardEvent.DOM_KEY_LOCATION_RIGHT && key.getAttribute("location") == "r") {
                key.classList.add("darker");
                updateKey(key)
            } else if(event.location === KeyboardEvent.DOM_KEY_LOCATION_STANDARD) {
                key.classList.add("darker");
                updateKey(key)
            }
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
    document.getElementById('search-bar').value = ""
  }




document.addEventListener('DOMContentLoaded', () => {
    document.getElementById("search-bar").addEventListener('input', (event) => {
        const modules = tungstenBridge.getFilteredModulesByPartialName(event.target.value)
        const search_results = document.getElementById("body-elements");
        search_results.innerHTML = ""
        for(const module of modules){
            const mod = document.createElement("div");
            mod.innerHTML = `<h3 class="rj">${module}</h3>`
            mod.classList.add("module");
            search_results.appendChild(mod);
            mod.setAttribute("ng_modulename", module);
            mod.addEventListener('click', (event_t) => {
                tungstenBridge.updateKeybind(mod.getAttribute("ng_modulename"), parseInt(window.currentkey));
                mod.classList.add("clicked");
            });
        }
    })
})

function updateKey(key){
    const blist = document.getElementById("binds-list");

    document.getElementById("keyname").innerHTML = key.innerHTML;

    blist.innerHTML = "";
    window.currentkey = key.getAttribute("keycode");

    let binds = tungstenBridge.getModulesByKeycode(parseInt(key.getAttribute("keycode")));
    console.log(binds);

    for(let bind of binds){
        let bind_element = document.createElement("h3");
        bind_element.classList.add("bind-indicator");
        bind_element.innerHTML = bind;
        blist.appendChild(bind_element);
    }

}

