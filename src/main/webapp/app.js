ssp = Ext.create('Ext.app.Application', {
	
	requires: ['Ext.container.Viewport',
			   'Ssp.model.StudentTO',
			   'Ssp.model.security.UserTO',
			   'Ssp.model.tool.studentintake.StudentEducationGoal',
			   'Ssp.model.tool.studentintake.StudentEducationPlan'],
			   
    name: 'Ssp',
    appFolder: 'app',
			
	stores: [ 'Students', 
			  'ApplicationForms', 
			  'Tools',
			  'security.Roles',
			  'reference.States', 
			  'reference.Challenges',
			  'reference.ChildCareArrangements',
			  'reference.Citizenships',
			  'reference.EducationalGoals',
			  'reference.EducationLevels',
			  'reference.EmploymentShifts',
			  'reference.Ethnicities',
			  'reference.FundingSources',
			  'reference.Genders',
			  'reference.MaritalStatuses',
			  'reference.VeteranStatuses',
			  'reference.YesNo'], 
	
	controllers: [
        	'Search','Tool','Main'
    ],
          		
    launch: function( app ) {
		
		// Define a global student model
		Ext.apply( this, {currentStudent: new Ssp.model.StudentTO({id:"0"}, {}),
						  currentUser: new Ssp.model.security.UserTO({id:"0"})
						});
		
		
		// Load the initial data for the application
		Ext.getStore('Students').load();
		Ext.getStore('security.Roles').load();

               					 		
   		// Build the UI
        Ext.create('Ext.container.Viewport', {
            layout: 'fit',
            id: 'sspView',
            items: []
        });   
 
 		// Display the application
 		this.getController('Main').displayApplication();

        
		// Prepare a form to be loaded and used dynamically
		/*
		var cleaner = Ext.create( 'Ssp.util.TemplateDataUtil' );
		var applicationFormsStore =  Ext.getStore('ApplicationForms');
        var formItems = cleaner.prepareTemplateData(  applicationFormsStore );
        */	
        
		/*
		 * Loads a template within a container using a store for the data model of the template.
		 
		var templateLoader = Ext.create( 'Ssp.util.Util' );
		
		var search = Ext.create('Ext.container.Container', {
			collapsible: true,
			baseCls: 'sspPanel',
		    store: Ext.getStore('Students'),
		    width: 300,
		    loader: {
		        url:'templates/students.html',
		        autoLoad: true,
		        renderer: templateLoader.loaderXTemplateRenderer
		    }
		    
		});	 
		*/ 
	    
   }
    
});