
function handleIncreasePrice(){
    let total = parseInt(document.getElementById('input-level').value);
    total += 5;
    console.log("total: ",total + "---- " + "typeOf total:",typeof (total))
    document.getElementById('input-level').value=total;
}


function handleDecreasePrice(){
    let total = parseInt(document.getElementById('input-level').value);
    total -= 5;
    if(total<0){
        alert("Vui lòng không hạ đấu giá quá 0 USD");
        total += 5;
    }
    console.log("total: ",total + " --- " + "typeOf total:",typeof (total))
    document.getElementById('input-level').value=total;
}
