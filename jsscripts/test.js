function getName(){
    return "jstest";
}

function process(arg, event){
    //调用java的println
    console.log(arg);



    event.sender.sendMessage("test");
}