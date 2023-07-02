


function instanceHWND(title){
    const newWindow = document.createElement("div");
    newWindow.classList.add("hwnd-window");
    newWindow.setAttribute("category", title);
    newWindow.style.top = tungstenBridge.getWindowY(title) + "px";
    newWindow.style.left = tungstenBridge.getWindowX(title) + "px";
    
    newWindow.innerHTML = `
    <div class="hwnd-head">
      <h3 class="title">${title}</h3>
    </div>
    <div class="hwnd-content">
  
    </div>
    `;

    document.body.appendChild(newWindow);
    finishSetupWindow(newWindow, title);
}

async function toggle(element){
  if(element.classList.contains("hwnd-collapsed")){
      element.classList.remove("hwnd-collapsed");
      element.querySelector(".hwnd-content").style = "display:block;";
      
  }else{
    element.classList.add("hwnd-collapsed");
    element.querySelector(".hwnd-content").style = "display:none";
  }
}

function sleep(ms) {
  return new Promise(resolve => setTimeout(resolve, ms));
}

document.addEventListener('contextmenu', function(event) {event.preventDefault()})

function finishSetupWindow(element, title) {
    let pos1 = 0,
        pos2 = 0,
        pos3 = 0,
        pos4 = 0;


    element.querySelector(".hwnd-head").onmousedown = dragMouseDown;
    element.querySelector(".hwnd-head").addEventListener('contextmenu', function(event) {event.preventDefault()})
    element.querySelector(".hwnd-head").onmouseup = function(event) {
      if (event.button === 2) {
        event.preventDefault();
        toggle(element);
      }
    };

    function dragMouseDown(e) {
      e = e || window.event;
      e.preventDefault();
      pos3 = e.clientX;
      pos4 = e.clientY;
      document.onmouseup = closeDragElement;
      document.onmousemove = elementDrag;
    }

    function elementDrag(e) {
      e = e || window.event;
      e.preventDefault();
      pos1 = pos3 - e.clientX;
      pos2 = pos4 - e.clientY;
      pos3 = e.clientX;
      pos4 = e.clientY;
      element.style.top = element.offsetTop - pos2 + "px";
      element.style.left = element.offsetLeft - pos1 + "px";
      tungstenBridge.setWindowPosition(title, element.offsetLeft - pos1, element.offsetTop - pos2);
    }

    function closeDragElement() {
      document.onmouseup = null;
      document.onmousemove = null;
    }
}

function expandModuleEvent(module){
  module.querySelector(".body").style = "display:block";
}

function contractModuleEvent(module){
  module.querySelector(".body").style = "display:none";
}

function toggleModuleEvent(id){
  tungstenBridge.toggleModule(id);
  for(let module of document.querySelectorAll(".module")){
    if(tungstenBridge.queryEnabled(module.getAttribute("id"))){
      module.classList.add("module-enabled");
    }else{
      if(module.classList.contains("module-enabled")){
        module.classList.remove("module-enabled");
      }
    }
  }
}

function initModule(module){
  module.onmouseup = function(event) {

    var target = event.target;
    var isBodyElementClicked = false;
  
    while (target !== module) {
      if (target.classList.contains('body')) {
        isBodyElementClicked = true;
        break;
      }
      target = target.parentNode;
    }

    if (isBodyElementClicked) {
      event.stopPropagation();
    } else {
      if (event.button == 2) {
        if (module.classList.contains("expanded")) {
          module.classList.remove("expanded");
          contractModuleEvent(module);
        } else {
          module.classList.add("expanded");
          expandModuleEvent(module);
        }
      } else if (event.button == 0) {
        if (!event.target.classList.contains('child')) {
          toggleModuleEvent(module.id);
        }
      }
    }
  };
}

function setup_textbox(name, module, element){
  element.addEventListener('input', (event) => {
    tungstenBridge.broadcastTextboxUpdate(name, module, event.target.value)
  })
}

function setup_button(name, module, element){
  element.addEventListener('click', (event) => {
    tungstenBridge.broadcastButtonClick(name, module)
  })
}

function setup_slider(name, module, element){
  element.addEventListener('input', (event) => {
    tungstenBridge.broadcastSliderUpdate(name, module, event.target.value)
  })
}

function setup_checkbox(name, module, element){
  element.addEventListener('change', (event) => {
    tungstenBridge.broadcastCheckboxUpdate(name, module, event.target.checked)
  })
}

function instanceModule(parent_category, module_name){

  let module = document.createElement("div");
  module.classList.add("module");
  module.setAttribute("id", module_name);
  let html = `
  <h3 class="module-title">${module_name}</h3>
  <p class="module-description">${tungstenBridge.getDescription(module_name)}</p>
  <div class="body" style="display:none">
    <h4 style="margin-bottom:5px;">Settings</h4>
  </div>
  `
  module.innerHTML = html;

  document.querySelector(`div[category=${parent_category}]`).querySelector(".hwnd-content").appendChild(module);

  let settings = tungstenBridge.getSettingHTML(module_name);
  for(let setting of settings){
    let div = document.createElement("div");
    div.innerHTML = setting;
    module.querySelector(".body").appendChild(div);
  }

  let bodyr = module.querySelector(".body");
  for(let i = 1; i < bodyr.children.length; i++){
    let setting = bodyr.children[i];
    let element_descriptor = setting.children[0];
    let body = setting.children[1];
    let setting_name = element_descriptor.innerHTML;
    tungstenBridge.print(body.tagName);
    if(body.tagName.toLowerCase()=="button"){
      setup_button(setting_name, module_name, body);
    }else if(body.tagName.toLowerCase()=="input"){
      switch(body.getAttribute("type").toLowerCase()){
        case "text":
          setup_textbox(setting_name, module_name, body);
        break;

        case "range":
          setup_slider(setting_name, module_name, body);
        break;
      }
    }else if(body.tagName.toLowerCase()=="label"){
      let checkbox = body.children[0];
      setup_checkbox(setting_name, module_name, checkbox);
    }
  }

  module.onmouseup = function(event) {

    var target = event.target;
    var isBodyElementClicked = false;
  
    while (target !== module) {
      if (target.classList.contains('body')) {
        isBodyElementClicked = true;
        break;
      }
      target = target.parentNode;
    }

    if (isBodyElementClicked) {
      event.stopPropagation();
    } else {
      if (event.button == 2) {
        if (module.classList.contains("expanded")) {
          module.classList.remove("expanded");
          contractModuleEvent(module);
        } else {
          module.classList.add("expanded");
          expandModuleEvent(module);
        }
      } else if (event.button == 0) {
        if (!event.target.classList.contains('child')) {
          toggleModuleEvent(module.id);
        }
      }
    }
  };
}



async function setup(){
  await sleep(50);
  tungstenBridge.print("Hello from Tungstenbridge");
  for(let moduleType of tungstenBridge.getModuleTypes()){
    tungstenBridge.print(moduleType);
    instanceHWND(moduleType);
    for(let module of tungstenBridge.getModulesByType(moduleType)){
      tungstenBridge.print("MODULE:" + module);
      instanceModule(moduleType, module);
    }
  }
}


setup();





  

  