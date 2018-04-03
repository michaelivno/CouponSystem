angular
		.module('CouponSystemApplication', [ 'ngResource' ])
		.controller(
				'CouponsController',
				function($resource) {

					var p = this;

					// Admin Login Url.
					p.adminLoginResource = $resource(
							"api/admin/login/:username/:password", {
								"username" : "@username",
								"password" : "@password"
							});

					// Company Login Url.
					p.companyLoginResource = $resource(
							"api/company/login/:username/:password", {
								"username" : "@username",
								"password" : "@password"
							});

					// Customer Login Url.
					p.customerLoginResource = $resource(
							"api/customer/login/:username/:password", {
								"username" : "@username",
								"password" : "@password"
							});

					// User Login object.
					p.user = {
						"username" : "",
						"password" : ""
					};

					// Users Dives Boolien.
					p.isLoggedInAdmin = false;
					p.isLoggedInCompany = false;
					p.isLoggedInCustomer = false;
					p.hideLogin = false;

					// Admin Login();
					p.loginAdmin = function() {
						p.adminLoginResource.get(p.user, function(response) {
							p.isLoggedInAdmin = true;
							p.hideLogin = true;
							p.info("Successful login as Admin");
						}, function(response) {
							p.handleError(response,
									"Error while logging in as Admin");
						});
					}

					// Company Login();
					p.loginCompany = function() {
						p.companyLoginResource.get(p.user, function(response) {
							p.isLoggedInCompany = true;
							p.hideLogin = true;
							p.info("Successful login as Company");
						}, function(response) {
							p.handleError(response,
									"Error while logging in as Company");
						});
					}

					// Customre Login();
					p.loginCustomer = function() {
						p.customerLoginResource.get(p.user, function(response) {
							p.isLoggedInCustomer = true;
							p.hideLogin = true;
							p.info("Successful login as Customer");
						}, function(response) {
							p.handleError(response,
									"Error while logging in as Customer");
						});
					}

					// Company GET method URL.
					p.CompanyUrl = $resource("api/admin/company/:id", {
						"id" : "@id"
					}, {
						"updateCompany" : {
							method : "PUT"
						}
					});

					// Customer GET method URL.
					p.CustomerUrl = $resource("api/admin/customer/:id", {
						"id" : "@id"
					}, {
						"updateCustomer" : {
							method : "PUT"
						}
					});

					// Coupon GET method URL.
					p.CouponUrl = $resource("api/company/coupon/:id", {
						"id" : "@id"
					}, {
						"updateCoupon" : {
							method : "PUT"
						}
					})

					// Some hellper div boolien.
					p.cCompany = false;
					p.compCreate = function() {
						if (p.cCompany == true) {
							p.cCompany = false;
						} else {
							p.cCompany = true;
						}
					}
					p.cCoupon = false;
					p.CoupCreate = function() {
						if (p.cCoupon == true) {
							p.cCoupon = false;
						} else {
							p.cCoupon = true;
						}
					}
					p.cCustomer = false;
					p.custCreate = function() {
						if (p.cCustomer == true) {
							p.cCustomer = false;
						} else {
							p.cCustomer = true;
						}
					}

					p.moreInfoDiv = function(coupon) {

						if (coupon.moreInfo == true) {
							coupon.moreInfo = false;
						} else {
							coupon.moreInfo = true;
						}

					}
					
					p.customerDiv= false;

					p.newComp= {
							"compName" : "",
							"password" : "",
							"email" : ""
						};

					
					// Create Company.
					p.createCompany = function(newComp) {
						p.CompanyUrl.save(p.newComp, function(item) {
									p.newComp.compName=item.compName;
									p.newComp.password=item.password;
									p.newComp.email=item.email;
									p.newComp = {
											"compName" : "",
											"password" : "",
											"email" : ""
										};
							p.loadCompaniesHelper();
							p.info("successfully create Company");
						}, function(response) {
							p.loadCompaniesHelper();
							p.handleError(response,
									"Error while Create Company");
						});
					}

					p.newCoup={
							"title":"",
							"endDate":"",
							"amount":"",
							"type":"",
							"message":"",
							"price":"",
							"image":""
					}
					
					// Create Coupon.
					p.createCoupon = function(newCoup) {
						p.CouponUrl.save(newCoup, function(item) {
						        	p.newCoup.title= item.title;
									p.newCoup.endDate= item.endDate;
									p.newCoup.amount= item.amount;
									p.newCoup.type= item.type;
									p.newCoup.message= item.message;
									p.newCoup.price= item.price;
									p.newCoup.image= item.image;
							
							p.newCoup={
									"title":"",
									"endDate":"",
									"amount":"",
									"type":"",
									"message":"",
									"price":"",
									"image":""
							};
							p.loadCouponsHelper();
							p.info("successfully create Coupon");
						}, function(response) {
							p.loadCouponsHelper();
							p
									.handleError(response,
											"Error while Create Coupon");
						});
					}

					
					p.newCust= {
							"custName" : "",
							"password" : ""
						};
					
					// Create Customer();
					p.createCustomer = function(newCust) {
						p.CustomerUrl.save(p.newCust, function(item) {
							p.newCust.custName = item.custName;
							p.newCust.password = item.password;
							p.newCust = {
									'custName' : '',
									'password' : ''
								};
							p.loadCustomersHelper();
							p.info("successfully create Customer");
						}, function(response) {
							p.loadCustomersHelper();
							p.handleError(response,
									"Error while create Customer");
						});
					}

					// Purchase Coupon();
					p.PurchaseUrl = $resource("api/customer/coupons");
					p.pruchaseCoupon = function(coupon) {
						p.PurchaseUrl.save(coupon.id, function() {
							p.custloadPurchCouponsHelper;
							p.info("successfully purchase Coupon");
						}, function(response) {
							p.handleError(response,
									"Error while purchase Coupon");
							p.custloadPurchCouponsHelper;
						});
					}

					// Get All Companies();
					p.allCompanies = [];
					p.loadCompanies = function() {
						p.allCompanies = p.CompanyUrl
								.query(
										function() {
											if (p.allCompanies !== null) {
												p.allCompanies
														.forEach(function(
																company) {
															company.edit = false;
														});
												p
														.info("successfully get all Companies");
											} else {
												p
														.info("successfully get all Companies But is no Companies ");
											}
										},
										function(response) {
											p
													.handleError(response,
															"Error while get all Companies");
										});
					}

					// Get All Companies helper();
					p.loadCompaniesHelper = function() {
						p.allCompanies = p.CompanyUrl.query(function() {
							p.allCompanies.forEach(function(company) {
								company.edit = false;
							});
						}, function(response) {
						});
					}

					// Get All Coupons.
					p.allCoupons = [];
					p.loadCoupons = function() {
						p.allCoupons = p.CouponUrl.query(function() {
							p.allCoupons.forEach(function(coupon) {
								coupon.edit = false;
								coupon.moreInfo = false;
								p.allCompanyCouponsDiv = true;
							});
							p.info("successfully get all Coupons");
						}, function(response) {
							p.handleError(response,
									"Error while get all Coupons");
						});
					}

					// Get All Coupons helper.
					p.loadCouponsHelper = function() {
						p.allCoupons = p.CouponUrl.query(function() {
							p.allCoupons.forEach(function(coupon) {
								coupon.edit = false;
							});
						}, function(response) {
						});
					}

					// Get All Customers.
					p.allCustomers = [];
					p.loadCustomers = function() {
						p.allCustomers = p.CustomerUrl.query(function() {
							p.allCustomers.forEach(function(customer) {
								customer.edit = false;
							});
							p.info("successfully get all Customers");
						}, function(response) {
							p.handleError(response,
									"Error while get all Customers");
						});
					}

					// Get All Customers helper.
					p.loadCustomersHelper = function() {
						p.allCustomers = p.CustomerUrl.query(function() {
							p.allCustomers.forEach(function(customer) {
								customer.edit = false;
							});
						});
					}

					// URL for Get Customer Coupons.
					p.CustomerServUrl = $resource("api/customer/coupons");

					// Get All customerCoupons.
					p.custAllCoupons = [];
					p.custloadCoupons = function() {
						p.custAllCoupons = p.CustomerServUrl.query(function() {
							p.custAllCoupons.forEach(function(coupon) {
								p.custAllCoupDiv=true;
								coupon.edit = false;
							});
							p.info("successfully get all Customer Coupons");
						}, function(response) {
							p.handleError(response,
									"Error while get all Customer Coupons");
						});
					}

					// Date Object for Get Coupons by Date.
					p.date = {
						'date' : ''
					};

					// URL for get Company coupons by Date.
					p.CompanyServDateUrl = $resource(
							"api/company/coupon/date/:date", {
								"date" : "@date"
							});

					// Get Company Coupons by Date.
					p.getCouponsByDateDiv = false;
					p.compDateAllCoupons = [];
					p.comploadDateCoupons = function() {
						p.getCouponsByDateDiv = true;
						p.compDateAllCoupons = p.CompanyServDateUrl
								.query(
										p.date,
										function() {
											p.date = {
													'date' : ''
												};
											p.compDateAllCoupons
													.forEach(function(coupon) {
														coupon.edit = false;
														p.info("successfully get coupon by date");
													});
										},
										function(response) {
											p
													.handleError(response,
															"Error while geting coupons by date!");
										});
					}

					// Get All purchased coupons.
					p.CustomerServPurchUrl = $resource("api/customer/coupons/purch");
					p.custPurchAllCoupons = [];
					p.custloadPurchCoupons = function() {
						p.custPurchAllCoupons = p.CustomerServPurchUrl
								.query(function() {
									p.custPurchAllCoupons
											.forEach(
													function(coupon) {
														p.custAllPurchDiv=true;
														coupon.edit = false;
														p
																.info("successfully Get all Purchase coupons.");
													},
													function(response) {
														p
																.handleError(
																		response,
																		"Error Get all Purchase coupon!");
													});
								});
					}

					// Get All purchased coupons helper.
					p.custloadPurchCouponsHelper = function() {
						p.custPurchAllCoupons = p.CustomerServPurchUrl
								.query(function() {
									p.custPurchAllCoupons.forEach(function(
											coupon) {
										coupon.edit = false;
									});
								});
					}

					// Get company coupons by Type URL.
					p.CompanyServTypeUrl = $resource(
							"api/company/coupon/type/:type", {
								"type" : "@type"
							});

					// Get company coupons by Type.
					p.getCouponsByDateDiv = false;
					p.compTypeAllCoupons = [];
					p.comploadTypeCoupons = function() {
						p.compTypeAllCoupons = p.CompanyServTypeUrl
								.query(
										p.type,
										function() {
											p.getCouponsByTypeDiv = true;
											p.compTypeAllCoupons
													.forEach(function(coupon) {
														coupon.edit = false;
														p
																.info("successfully get coupons by Type.");
													});
										},
										function(response) {
											p
													.handleError(response,
															"Error while geting coupons by type!");
										});
					}

					// Type object for (getByType) functions.
					p.type = {
						"type" : ""
					};

					// Get customer coupons by Type URL.
					p.CustomerServTypeUrl = $resource(
							"api/customer/coupons/type/:type", {
								"type" : "@type"
							});

					// Get customer coupons by type.
					p.custTypeAllCoupons = [];
					p.custloadTypeCoupons = function() {
						p.custTypeAllCoupons = p.CustomerServTypeUrl
								.query(
										p.type,
										function() {
											p.custTypeAllCoupons
													.forEach(function(coupon) {
														p.custTypeDiv=true;
														coupon.edit = false;
														p
																.info("successfully get coupons by Type.");
													});
										},
										function(response) {
											p
													.handleError(response,
															"Error while geting coupons by type!");
										});
					}

					// Price object for (getByPrice) function.
					p.price = {
						"price" : ""
					};

					// Get customer coupons by price URL.
					p.CustomerServPriceUrl = $resource(
							"api/customer/coupons/price/:price", {
								"price" : "@price"
							});

					// Get customer coupons by price.
					p.custPriceAllCoupons = [];
					p.custloadPriceCoupons = function() {
						p.custPriceAllCoupons = p.CustomerServPriceUrl
								.query(
										p.price,
										function() {
											p.custPriceAllCoupons
													.forEach(function(coupon) {
														p.custPriceDiv=true;
														coupon.edit = false;
													});
											p
													.info("successfully get Coupons By Price.");
										},
										function(response) {
											p
													.handleError(response,
															"Error while Coupons By Price");
										});
					}

					// Get Company coupons by price URL.
					p.CompServPriceUrl = $resource(
							"api/company/coupon/price/:price", {
								"price" : "@price"
							});

					// Get Company coupons by price.
					p.getCouponsByPriceDiv = false;
					p.compPriceAllCoupons = [];
					p.comploadPriceCoupons = function() {
						p.compPriceAllCoupons = p.CompServPriceUrl
								.query(
										p.price,
										function() {
											p.getCouponsByPriceDiv = true;
											p.compPriceAllCoupons
													.forEach(function(coupon) {
														coupon.edit = false;
													});
											p
													.info("successfully get Coupons By Price.");
										},
										function(response) {
											p
													.handleError(response,
															"Error while Coupons By Price");
										});
					}

					// Remove Company.
					p.removeCompany = function(company) {
						company.$delete(function() {
							p.loadCompaniesHelper();
							p.info("successfully deleted Company");
						}, function(response) {
							p.loadCompaniesHelper();
							p.handleError(response,
									"Error while deleting Company");
						});
					}

					// Remove Coupon.
					p.removeCoupon = function(coupon) {
						coupon.$delete(function() {
							p.loadCouponsHelper();
							p.info("successfully deleted Coupon");
						}, function(response) {
							p.loadCouponsHelper();
							p.handleError(response,
									"Error while deleting coupon");
						});
					}

					// Remove Customer.
					p.removeCustomer = function(customer) {
						customer.$delete(function() {
							p.loadCustomersHelper();
							p.info("successfully deleted Customer");
						}, function(response) {
							p.loadCustomersHelper();
							p.handleError(response,
									"Error while deleting Customer");
						});
					}

					// Update Company.
					p.compUpdate = function(company) {
						company.edit = !company.edit;
					}
					p.compInvokeUpdate = function(company) {
						const updateCompany = company;

						delete updateCompany.edit;

						updateCompany.$updateCompany(function() {
							p.loadCompaniesHelper();
							p.info("Successfully updated company");
						}, function(response) {
							p.loadCompaniesHelper();
							p.handleError(response,
									"Error while updating company");
						});
					}

					// Update Coupon.
					p.coupUpdate = function(coupon) {
						coupon.edit = !coupon.edit;
					}
					p.coupInvokeUpdate = function(coupon) {
						const updateCoupon = coupon;
						delete updateCoupon.edit;

						updateCoupon.$updateCoupon(function() {
							p.loadCouponsHelper();
							p.info("Successfully updated Coupon");
						}, function(response) {
							p.loadCouponsHelper();
							p.handleError(response,
									"Error while updating coupon");
						});
					}

					// Update Customer.
					p.custUpdate = function(customer) {
						customer.edit = !customer.edit;
					}
					p.custInvokeUpdate = function(customer) {
						const updateCustomer = customer;

						delete updateCustomer.edit;

						updateCustomer.$updateCustomer(function() {
							p.loadCustomersHelper();
							p.info("Successfully updated Customer");
						}, function(response) {
							p.loadCustomersHelper();
							p.handleError(response,
									"Error while updating Customer");
						});
					}

					// Company Object for (getById) function.
					p.company = {
						'company.id' : '',
						'company.compName' : '',
						'company.password' : '',
						'company.email' : ''
					};

					// Customer Object for (getById) function.
					p.customer = {
						'customer.id' : '',
						'customer.custName' : '',
						'customer.password' : '',
					};

					// ID object for (getById) function.
					p.CompReq = {
						'id' : ''
					};
					

					// Get company by ID.
					p.showGetCompay = false;
					p.getCompany = function() {
						p.CompanyUrl.get(p.CompReq, function(item) {
							p.company = item;
							p.showGetCompay = true;
							p.company.edit = true;
							p.info("sucessfully reloaded company");
						}, function(response) {
							p.handleError(response,
									"Error while getting Company");
						});
					}

					p.CusReq = {
							'id' : ''
					};
					
					// Get Customer by ID.
					p.showGetCustomer = false;
					p.getCustomer = function() {
						p.CustomerUrl.get(p.CusReq, function(item) {
							p.customer = item;
							p.showGetCustomer = true;
							p.customer.edit = true;
							p.info("sucessfully reloaded Customer");
						}, function(response) {
							p.handleError(response,
									"Error while getting Customer");
						});
					}

					p.logoutAll = $resource("api/admin/logout");

					p.logout = function() {
						p.logoutAll.get(function() {
							p.isLoggedInAdmin = false;
							p.isLoggedInCompany = false;
							p.isLoggedInCustomer = false;
							p.allCompanyCouponsDiv = false;
							p.getCouponsByDateDiv = false;
							p.getCouponsByTypeDiv = false;
							p.getCouponsByPriceDiv = false;
							p.custAllCoupDiv=false;
							p.custAllPurchDiv=false;
							p.custTypeDiv=false;
							p.custPriceDiv=false;
							p.user = {
								"username" : "",
								"password" : ""
							};
							p.CusReq = {
									'id' : ''
							};
							p.CompReq = {
									'id' : ''
							};
							p.price = {
									"price" : ""
								};
							p.type = {
									"type" : ""
								};
							p.date = {
									'date' : ''
								};
							p.hideLogin = false;
							p.info("Successful logout");
						}, function(response) {
							p.handleError(response, "Error while logginout");
						});

					};

					p.infoMessage = "";
					p.errorMessage = "";

					p.info = function(message) {
						p.infoMessage = message;
						p.errorMessage = "";
					}

					p.error = function(message) {
						p.infoMessage = "";
						p.errorMessage = message;
					}

					p.handleError = function(response, defaultMessage) {
						if (response.data != null
								&& response.data.message != null) {
							p.error("Error: ");
						} else {
							p.error(defaultMessage);
						}
					}

				});
