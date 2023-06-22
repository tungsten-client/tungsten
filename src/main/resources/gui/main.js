


function instanceHWND(){
    const newWindow = document.createElement("div");
    newWindow.classList.add("hwnd-window");
    newWindow.style.top = Math.random() * (screen.clientHeight - 200) + "px";
    newWindow.style.left = Math.random() * (screen.clientWidth - 200) + "px";

    newWindow.innerHTML = `

    `;

    screen.appendChild(newWindow);
    finishSetupWindow(newWindow);
}


function finishSetupWindow(element) {
    let pos1 = 0,
        pos2 = 0,
        pos3 = 0,
        pos4 = 0;

    element.querySelector(".hwnd-head").onmousedown = dragMouseDown;
    element.querySelector(".hwnd-head").addEventListener('contextmenu', function(event) {
        event.preventDefault(); // Prevent the default right-click behavior
        if(element.classList.contains("hwnd-collapsed")){
            element.classList.remove("hwnd-collapsed");
            element.querySelector(".hwnd-content").style = "display:block";
        }else{
            element.classList.add("hwnd-collapsed");
            element.querySelector(".hwnd-content").style = "display:none";
        }
    });

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
    }

    function closeDragElement() {
      document.onmouseup = null;
      document.onmousemove = null;
    }
}
  
      // Initialize the draggable div
      finishSetupWindow(document.querySelector(".hwnd-window"));
  

  