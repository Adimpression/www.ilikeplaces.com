bef = function () {
    $.blockUI()
};
aft = function () {
    $.unblockUI()
};
function EventMonitor() {
    this.before = a;
    this.after = b;
    this.monitor = document.getElementById("monitorId");
    this.count = 0;
    post_fun_arg = {msg: "Your Request Was Completed Successfully!"};
    post_fun = function (c) {
    };
    function a(c) {
        if (this.count == 0) {
            $("#monitorId").show();
            alert(c.pageX + "," + c.pageY)
        }
        this.count++
    }

    function b(c) {
        if (this.count == 0) {
            return
        }
        this.count--;
        if (this.count == 0) {
            $("#monitorId").hide()
        }
        post_fun(post_fun_arg)
    }

    post_fun_arg = {};
    post_fun = function (c) {
    }
};