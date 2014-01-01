/*******************************************************************************
 *
 *  Copyright 2011 - Sardegna Ricerche, Distretto ICT, Pula, Italy
 *
 * Licensed under the EUPL, Version 1.1.
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 *
 *  http://www.osor.eu/eupl
 *
 * Unless required by applicable law or agreed to in  writing, software distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and limitations under the Licence.
 * In case of controversy the competent court is the Court of Cagliari (Italy).
 *******************************************************************************/
/*
 * @class Structure
 * @parent Backbone.Model
 * @constructor
 * Model to hold a structure.
 * @tag models
 * @author LabOpenSource
 */
window.Structure = Backbone.Model.extend({
    defaults: {
    	sub_description: null
    },
    initialize: function () {
        this.get("notes") ? this.set({
            sub_description: this.get("notes").substring(0, 20) + '...'
        }) : this.set({
            sub_description: null
        });
    },
    url: function () {
        var base = this.urlRoot;
        if (this.isNew()) return base;
        base = base + (base.charAt(base.length - 1) == '/' ? '' : '/') + encodeURIComponent(this.id);
        base = base + "?id_propertyType="+this.attributes.id_propertyType;
        return base;
    },
    urlRoot: "rest/structures/",
    validate: function (attrs) {},
});

window.Autocompletes = AutocompleteCollection.extend({
    url: function () {
        return 'rest/structures/suggest' + this.term;
    }
});