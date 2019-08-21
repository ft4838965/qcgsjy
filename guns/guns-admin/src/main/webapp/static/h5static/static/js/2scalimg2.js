	var system = navigator.userAgent;
    var ie7 = new RegExp("MSIE 7.0").exec(system);
    var ie8 = new RegExp("MSIE 8.0").exec(system);
    var ie9 = new RegExp("MSIE 9.0").exec(system);
    var win = new RegExp("Windows").exec(system);
    var handledown = win? 'mousedown' : 'touchstart';
    var handlemove = win? 'mousemove' : 'touchmove';
    var handleup   = win? 'mouseup'   : 'touchend';
    

	function getClass(className){
		this.all = document.getElementsByTagName('*');
		this.arr = [];
		for(var i=0;i<this.all.length;i++){
			if(all[i].className.indexOf(className) != -1){
				this.arr.push(all[i]);
			};
		}
		return this.arr;
	}
	function addClass(element,className){
		if(element.className.indexOf(className) == -1){
			element.className += " "+className;
		}
	}
	function delClass(element,className){
		if(element.className.indexOf(className) != -1){
			element.className = element.className.replace(" "+className,'');
		}
	}

	var scaleImg = function(fn){
		this.id = fn.ele;
		this.sid ='';
		this.pop = '';
		this.domArr = [];
		this.win = {width:0,height:0};
		this.sTop = 0;
		this.num =0;
		this.ln =0;
		this.N = 0;
		this.imgN=0;
		this.creat();
		this.setValue();
		this.scroll();
		this.resize();
	}
	scaleImg.prototype = {
		creat:function(){
			this.ln = this.id.children
			for(var i = 0;i<this.ln.length;i++){
				this.domArr.push({});
				this.domArr[i].ele = this.ln[i];
				this.tf(i)
			}
		},
		setValue:function(){
			this.sTop = document.body.scrollTop;
			this.win.width = window.innerWidth;
			this.win.height = window.innerHeight;

			for(var i = 0;i<this.domArr.length;i++){
				this.domArr[i].index = i;
				this.domArr[i].width = this.domArr[i].ele.clientWidth;
				this.domArr[i].height = this.domArr[i].ele.clientHeight;
				this.domArr[i].top = this.domArr[i].ele.offsetTop - this.sTop;
				this.domArr[i].left = this.domArr[i].ele.offsetLeft;
				this.domArr[i].maxSrc = this.domArr[i].ele.getElementsByTagName('img')[0].getAttribute('maxSrc');
			}
		},
		scroll:function(){
			document.body.onscroll = ()=>{
				
				this.setValue();
			}
		},
		resize:function(){
			window.onresize = ()=>{
				this.setValue();
				this.swiper(this.num);
			}
		},
		tf:function(index){
			var load = getClass('y_loading')[0];
			var id  = this.domArr[index].ele;
			id.addEventListener('touchstart',(event)=>{
				event.preventDefault;
				this.num = index;
				this.imgN = this.domArr.length;
				load.style.display = 'block';
				for(var i = 0;i<this.domArr.length;i++){
					this.imgSize(this.domArr[i].maxSrc,i,load)
				}
				return false;

			})
		},
		off:function(){
			var ismove = true;
			this.sid.addEventListener(handledown,function(event){
				ismove=true;
				event.preventDefault();
			})
			this.sid.addEventListener(handlemove,function(event){
				ismove = false;
				event.preventDefault();
			})
			this.sid.addEventListener(handleup,(event)=>{
				var index = this.num
				if(ismove){
					this.sid.style.display = 'none';
					event.preventDefault();
				}
				this.scroll();
			})
		},
		imgSize:function(src,index,load){
			var img = new Image();
			img.src = src;
			img.onload = ()=>{
				this.domArr[index].img = {w:img.width,h:img.height};			
				this.imgN--;
				if(!this.imgN){
					load.style.display = 'none';
					this.creatSlide();
					this.sid.style.display = 'block';
				}
			}
		},
		creatSlide:function(){
			getClass('swiper')[0].innerHTML = '<div class="swiper-container m'+this.N+'"><div class="swiper-wrapper"></div><div class="swiper-pagination p'+this.N+'"></div></div>'
			this.sid = getClass('swiper-container')[0];
			var ele = getClass('swiper-wrapper')[0];
			var inner = [];
			var sty='';
			for(var i =0 ;i<this.ln.length;i++){
				if(this.win.height/this.win.width < this.domArr[i].img.h/this.domArr[i].img.w ){
					sty = "height:100%"
				}else{
					sty = "width:100%"
				}
				inner.push('<div class="swiper-slide"><img style="'+sty+'" src="'+this.domArr[i].maxSrc+'"></div>');
			}
			//鐢熸垚slide鐨刣om缁撴瀯
			ele.innerHTML = inner.join(' ');
			this.sid.style.display = 'block';
			this.swiper(this.num);
			this.off();
			this.N++
		},
		swiper:function(index){
			var swiper = new Swiper('.m'+this.N, {
				pagination: {
					el: '.p'+this.N,
					type: 'fraction'
				},
		        paginationClickable: true,
		        initialSlide:index,
		    });
		}
	}
	function showImg(){
		var arr = []
		var eleArr = getClass('imgBox')
		for(var i = 0;i<eleArr.length;i++){
			arr[i] = new scaleImg({
				ele:getClass('imgBox')[i]		
			})
		}
	}
	showImg()