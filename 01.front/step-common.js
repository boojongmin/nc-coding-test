function el(id) {
    return document.getElementById(id);
}

function ce(tag, text) {
    var el = document.createElement(tag);
    var textNode = document.createTextNode(text)
    el.appendChild(textNode)
    return el;
}

function trimAndremoveDoubleQuote(text) {
    return text.trim().split('"')[1];
}

function removeAllChild(el) {
    var firstChild = el.firstChild
    while(firstChild)  {
        el.removeChild(firstChild);
        firstChild = el.firstChild
    }  
}

function initTable() {
    removeAllChild('step1-table-header');
    removeAllChild('step1-table-body');
    removeAllChild(el('step2-table-body'));
}