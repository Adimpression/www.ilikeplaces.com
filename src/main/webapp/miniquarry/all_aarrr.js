try {
    setTimeout("initialize_gmap_scripts_transparently()", 7000)
} catch (err) {
    alert(err);
    if (confirm("You internet connection seems to be flaky. Reload web page?")) {
        try {
            setTimeout("initialize_gmap_scripts_transparently()", 7000)
        } catch (err) {
            alert("Sorry, Still could not load required stuff.")
        }
    } else {
        alert("Ok. Not reloading. However things might not work!")
    }
}
(function (a) {
    a.prompt = function (p, q) {
        q = a.extend({}, a.prompt.defaults, q);
        a.prompt.currentPrefix = q.prefix;
        var e = (a.browser.msie && a.browser.version < 7);
        var g = a(document.body);
        var c = a(window);
        var b = '<div class="' + q.prefix + 'box" id="' + q.prefix + 'box">';
        if (q.useiframe && ((a("object, applet").length > 0) || e)) {
            b += '<iframe src="javascript:false;" style="display:block;position:absolute;z-index:-1;" class="' + q.prefix + 'fade" id="' + q.prefix + 'fade"></iframe>'
        } else {
            if (e) {
                a("select").css("visibility", "hidden")
            }
            b += '<div class="' + q.prefix + 'fade" id="' + q.prefix + 'fade"></div>'
        }
        b += '<div class="' + q.prefix + '" id="' + q.prefix + '"><div class="' + q.prefix + 'container"><div class="';
        b += q.prefix + 'close">X</div><div id="' + q.prefix + 'states"></div>';
        b += "</div></div></div>";
        var o = a(b).appendTo(g);
        var l = o.children("#" + q.prefix);
        var m = o.children("#" + q.prefix + "fade");
        if (p.constructor == String) {
            p = {state0: {html: p, buttons: q.buttons, focus: q.focus, submit: q.submit}}
        }
        var n = "";
        a.each(p, function (s, r) {
            r = a.extend({}, a.prompt.defaults.state, r);
            p[s] = r;
            n += '<div id="' + q.prefix + "_state_" + s + '" class="' + q.prefix + '_state" style="display:none;"><div class="' + q.prefix + 'message">' + r.html + '</div><div class="' + q.prefix + 'buttons">';
            a.each(r.buttons, function (u, t) {
                n += '<button name="' + q.prefix + "_" + s + "_button" + u + '" id="' + q.prefix + "_" + s + "_button" + u + '" value="' + t + '">' + u + "</button>"
            });
            n += "</div></div>"
        });
        l.find("#" + q.prefix + "states").html(n).children("." + q.prefix + "_state:first").css("display", "block");
        l.find("." + q.prefix + "buttons:empty").css("display", "none");
        a.each(p, function (t, s) {
            var r = l.find("#" + q.prefix + "_state_" + t);
            r.children("." + q.prefix + "buttons").children("button").click(function () {
                var w = r.children("." + q.prefix + "message");
                var u = s.buttons[a(this).text()];
                var x = {};
                a.each(l.find("#" + q.prefix + "states :input").serializeArray(), function (y, z) {
                    if (x[z.name] === undefined) {
                        x[z.name] = z.value
                    } else {
                        if (typeof x[z.name] == Array) {
                            x[z.name].push(z.value)
                        } else {
                            x[z.name] = [x[z.name], z.value]
                        }
                    }
                });
                var v = s.submit(u, w, x);
                if (v === undefined || v) {
                    d(true, u, w, x)
                }
            });
            r.find("." + q.prefix + "buttons button:eq(" + s.focus + ")").addClass(q.prefix + "defaultbutton")
        });
        var f = function () {
            o.css({top: c.scrollTop()})
        };
        var j = function () {
            if (q.persistent) {
                var s = 0;
                o.addClass(q.prefix + "warning");
                var r = setInterval(function () {
                    o.toggleClass(q.prefix + "warning");
                    if (s++ > 1) {
                        clearInterval(r);
                        o.removeClass(q.prefix + "warning")
                    }
                }, 100)
            } else {
                d()
            }
        };
        var h = function (u) {
            var t = (window.event) ? event.keyCode : u.keyCode;
            if (t == 27) {
                d()
            }
            if (t == 9) {
                var v = a(":input:enabled:visible", o);
                var s = !u.shiftKey && u.target == v[v.length - 1];
                var r = u.shiftKey && u.target == v[0];
                if (s || r) {
                    setTimeout(function () {
                        if (!v) {
                            return
                        }
                        var w = v[r === true ? v.length - 1 : 0];
                        if (w) {
                            w.focus()
                        }
                    }, 10);
                    return false
                }
            }
        };
        var i = function () {
            o.css({position: (e) ? "absolute" : "fixed", height: c.height(), width: "100%", top: (e) ? c.scrollTop() : 0, left: 0, right: 0, bottom: 0});
            m.css({position: "absolute", height: c.height(), width: "100%", top: 0, left: 0, right: 0, bottom: 0});
            l.css({position: "absolute", top: q.top, left: "50%", marginLeft: ((l.outerWidth() / 2) * -1)})
        };
        var k = function () {
            m.css({zIndex: q.zIndex, display: "none", opacity: q.opacity});
            l.css({zIndex: q.zIndex + 1, display: "none"});
            o.css({zIndex: q.zIndex})
        };
        var d = function (t, s, u, r) {
            l.remove();
            if (e) {
                g.unbind("scroll", f)
            }
            c.unbind("resize", i);
            m.fadeOut(q.overlayspeed, function () {
                m.unbind("click", j);
                m.remove();
                if (t) {
                    q.callback(s, u, r)
                }
                o.unbind("keypress", h);
                o.remove();
                if (e && !q.useiframe) {
                    a("select").css("visibility", "visible")
                }
            })
        };
        i();
        k();
        if (e) {
            c.scroll(f)
        }
        m.click(j);
        c.resize(i);
        o.bind("keydown keypress", h);
        l.find("." + q.prefix + "close").click(d);
        m.fadeIn(q.overlayspeed);
        l[q.show](q.promptspeed, q.loaded);
        if (q.timeout > 0) {
            setTimeout(a.prompt.close, q.timeout)
        }
        return l.find("#" + q.prefix + "states ." + q.prefix + "_state:first ." + q.prefix + "defaultbutton").focus()
    };
    a.prompt.defaults = {prefix: "jqi", buttons: {Ok: true}, loaded: function () {
    }, submit: function () {
        return true
    }, callback: function () {
    }, opacity: 0.6, zIndex: 999, overlayspeed: "slow", promptspeed: "fast", show: "fadeIn", focus: 0, useiframe: false, top: "15%", persistent: true, timeout: 0, state: {html: "", buttons: {Ok: true}, focus: 0, submit: function () {
        return true
    }}};
    a.prompt.currentPrefix = a.prompt.defaults.prefix;
    a.prompt.setDefaults = function (b) {
        a.prompt.defaults = a.extend({}, a.prompt.defaults, b)
    };
    a.prompt.setStateDefaults = function (b) {
        a.prompt.defaults.state = a.extend({}, a.prompt.defaults.state, b)
    };
    a.prompt.getStateContent = function (b) {
        return a("#" + a.prompt.currentPrefix + "_state_" + b)
    };
    a.prompt.getCurrentState = function () {
        return a("." + a.prompt.currentPrefix + "_state:visible")
    };
    a.prompt.getCurrentStateName = function () {
        var b = a.prompt.getCurrentState().attr("id");
        return b.replace(a.prompt.currentPrefix + "_state_", "")
    };
    a.prompt.goToState = function (b) {
        a("." + a.prompt.currentPrefix + "_state").slideUp("slow");
        a("#" + a.prompt.currentPrefix + "_state_" + b).slideDown("slow", function () {
            a(this).find("." + a.prompt.currentPrefix + "defaultbutton").focus()
        })
    };
    a.prompt.nextState = function () {
        var b = a("." + a.prompt.currentPrefix + "_state:visible").next();
        a("." + a.prompt.currentPrefix + "_state").slideUp("slow");
        b.slideDown("slow", function () {
            b.find("." + a.prompt.currentPrefix + "defaultbutton").focus()
        })
    };
    a.prompt.prevState = function () {
        var b = a("." + a.prompt.currentPrefix + "_state:visible").prev();
        a("." + a.prompt.currentPrefix + "_state").slideUp("slow");
        b.slideDown("slow", function () {
            b.find("." + a.prompt.currentPrefix + "defaultbutton").focus()
        })
    };
    a.prompt.close = function () {
        a("#" + a.prompt.currentPrefix + "box").fadeOut("fast", function () {
            a(this).remove()
        })
    }
})(jQuery);
function setCookie(a, d, b) {
    var e = new Date();
    e.setDate(e.getDate() + b);
    var c = escape(d) + ((b == null) ? "" : "; expires=" + e.toUTCString());
    document.cookie = a + "=" + c
}
function getCookie(b) {
    var c, a, e, d = document.cookie.split(";");
    for (c = 0; c < d.length; c++) {
        a = d[c].substr(0, d[c].indexOf("="));
        e = d[c].substr(d[c].indexOf("=") + 1);
        a = a.replace(/^\s+|\s+$/g, "");
        if (a == b) {
            return unescape(e)
        }
    }
}
function gup(b) {
    b = b.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var a = "[\\?&]" + b + "=([^&#]*)";
    var d = new RegExp(a);
    var c = d.exec(window.location.href);
    if (c == null) {
        return""
    } else {
        return c[1]
    }
}
(function (a) {
    a.fn.appear = function (d, b) {
        var c = a.extend({data: undefined, one: true}, b);
        return this.each(function () {
            var g = a(this);
            g.appeared = false;
            if (!d) {
                g.trigger("appear", c.data);
                return
            }
            var f = a(window);
            var e = function () {
                if (!g.is(":visible")) {
                    g.appeared = false;
                    return
                }
                var k = f.scrollLeft();
                var j = f.scrollTop();
                var l = g.offset();
                var i = l.left;
                var m = l.top;
                if (m + g.height() >= j && m <= j + f.height() && i + g.width() >= k && i <= k + f.width()) {
                    if (!g.appeared) {
                        g.trigger("appear", c.data)
                    }
                } else {
                    g.appeared = false
                }
            };
            var h = function () {
                g.appeared = true;
                if (c.one) {
                    f.unbind("scroll", e);
                    var j = a.inArray(e, a.fn.appear.checks);
                    if (j >= 0) {
                        a.fn.appear.checks.splice(j, 1)
                    }
                }
                d.apply(this, arguments)
            };
            if (c.one) {
                g.one("appear", c.data, h)
            } else {
                g.bind("appear", c.data, h)
            }
            f.scroll(e);
            a.fn.appear.checks.push(e);
            (e)()
        })
    };
    a.extend(a.fn.appear, {checks: [], timeout: null, checkAll: function () {
        var b = a.fn.appear.checks.length;
        if (b > 0) {
            while (b--) {
                (a.fn.appear.checks[b])()
            }
        }
    }, run: function () {
        if (a.fn.appear.timeout) {
            clearTimeout(a.fn.appear.timeout)
        }
        a.fn.appear.timeout = setTimeout(a.fn.appear.checkAll, 20)
    }});
    a.each(["append", "prepend", "after", "before", "attr", "removeAttr", "addClass", "removeClass", "toggleClass", "remove", "css", "show", "hide"], function (c, d) {
        var b = a.fn[d];
        if (b) {
            a.fn[d] = function () {
                var e = b.apply(this, arguments);
                a.fn.appear.run();
                return e
            }
        }
    })
})(jQuery);
$(document).ready(function () {
    $(".lose_attn").mouseenter(function () {
        $(".lose_attn").fadeTo("fast", 1)
    });
    $(".lose_attn").mouseleave(function () {
        $(".lose_attn").fadeTo("fast", 0.1)
    });
    var c = $("#impromptuPlaceDetails").html();
    $("#impromptuPlaceDetails").remove();
    impromptuPlaceDetailsPopup = {state0: {html: c, buttons: {"ADD PLACE": 0, Cancel: 1}, focus: 0, submit: function (e, d, h) {
        switch (e) {
            case 0:
                var i = $("#impromptuPlaceDetailsPlaceName").attr("value");
                i = i.substring(0, 29);
                var g = $("#impromptuPlaceDetailsPlaceDetails").attr("value");
                g = g.substring(0, 499);
                setNotification("Be patient while you are taken to the place you just created!");
                window.location.href = "/page/_org?category=143&woehint=" + lastSelectedLocation.lat + "," + lastSelectedLocation.lng + "&placename=" + i + "&placedetails=" + g;
                return false;
            case 1:
                $.prompt.close();
                return false
        }
    }}};
    $.prompt.defaults.opacity = 1;
    focusLat = 0;
    focusLng = 0;
    zoom = 2;
    try {
        var a = "http://www.geoplugin.net/json.gp?jsoncallback=?";
        $.getJSON(a, function (d) {
            focusLng = d.geoplugin_longitude;
            focusLat = d.geoplugin_latitude;
            zoom = d.geoplugin_city != null ? 11 : (d.geoplugin_region != null ? 9 : (d.geoplugin_countryName ? 6 : 2));
            browserLocation = (d.geoplugin_city != null ? d.geoplugin_city + "," : "") + (d.geoplugin_region != null ? d.geoplugin_region + "," : "") + (d.geoplugin_countryName != null ? d.geoplugin_countryName : "");
            document.getElementById("searchboxmap").value = browserLocation
        })
    } catch (b) {
        alert(b)
    }
    initialize_gmap_scripts();
    $(".ajax_image").appear(function () {
        this.src = this.title;
        this.title = ""
    });
    $("#Juice").mouseenter(function (d) {
        d.preventDefault();
        $("html,body").animate({scrollTop: $("#Juice").offset().top}, 500)
    });
    $(".click_indicator").hover(function () {
        $(this).fadeTo(0, 0.8)
    },function () {
        $(this).fadeTo(0, 1)
    }).click(function () {
        $(this).fadeTo(0, 0.3)
    });
    $(".click_indicator_strong").hover(function () {
        $(this).fadeTo(0, 0.5)
    },function () {
        $(this).fadeTo(0, 1)
    }).click(function () {
        $(this).fadeTo(0, 0.3)
    });
    $(".fillPageType").addClass($(".pageType").attr("value"))
});
focusMapWithLatLngLoop = undefined;
function focusMapWithLatLng() {
    try {
        if (gup("latitude") != "" && gup("longitude") != "") {
            map.setCenter(new google.maps.LatLng(gup("latitude"), gup("longitude")));
            if (gup("type") != "") {
                var a = gup("type");
                if (a == "building") {
                    map.setZoom(17)
                } else {
                    if (a == "area") {
                        map.setZoom(15)
                    } else {
                        if (a == "town") {
                            map.setZoom(13)
                        } else {
                            if (a == "city") {
                                map.setZoom(11)
                            } else {
                                map.setZoom(10)
                            }
                        }
                    }
                }
            }
            clearInterval(focusMapWithLatLngLoop);
            try {
                $.prompt.close()
            } catch (b) {
            }
        } else {
        }
    } catch (b) {
    }
}
focusMapWithLatLngLoop = setInterval("focusMapWithLatLng()", 2000);
zoomInHint = true;