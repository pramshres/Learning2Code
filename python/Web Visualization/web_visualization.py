import csv
import os.path
import pandas as pd
from datetime import datetime
import altair_viewer
import re
import tempfile
import altair as alt
from flask import Flask, render_template, request
import pydoc
import shutil

alt.renderers.enable('altair_viewer')

#Defaults
start_date = '21/02/2020'
end_date = '05/11/2020'
default_county = 'alle fylker.csv'

counties_dict = {
    'Alle fylker' : 'alle fylker.csv',
    'Agder' : 'agder.csv',
    'Innlandet' : 'innlandet.csv',
    'More og Romsdal' : 'more og romsdal.csv',
    'Nordland' : 'nordland.csv',
    'Oslo' : 'oslo.csv',
    'Rogaland' : 'rogaland.csv',
    'Troms og Finnmark' : 'troms og finnmark.csv',
    'Trondelag' : 'trondelag.csv',
    'Vestfold og Telemark' : 'vestfold og telemark.csv',
    'Vestland' : 'vestland.csv',
    'Viken' : 'viken.csv' }

def readCSV(csv, start, end):
    """
    Reads a CSV file and makes a dataframe containing only the data needed between two date ranges
    Args:
        csv (str): name of the csv file
        start (str): start date in format dd/mm/yyyy
        end (str): end date in format dd/mm/yyyy
    Returns:
        df (dataframe): dataframe containing the date (in datetime64), cumulative cases and new cases between start and end dates
    """
    df = pd.read_csv(csv, sep=';', index_col='Dato')
    df.index = df.index.map(lambda x: x.replace('.', '/'))
    df = df.loc[start : end]
    df = df.reset_index()

    #Covert to datetime64[ns]
    df['Dato'] = pd.to_datetime(df['Dato'], format='%d/%m/%Y')

    return df

def plot_reported_cases(county=default_county, start=start_date, end=end_date, display=True, display_both=False):
    """
    Plots a bar plot of reported cases and displays it
    Args:
        county (str): name of the csv file
        start (str): start date in format dd/mm/yyyy
        end (str): end date in format dd/mm/yyyy
        display (bool): if true, displays the plot with altair-viewer
        display_both (bool): if true, displays both the number of cumulative and new cases in a hover box
    Returns:
        chart (altair chart)
    """
    tooltip_holder = [alt.Tooltip('Dato:T', title='Date'), alt.Tooltip('Nye tilfeller', title='New Cases')]
    if (display_both == True):
         tooltip_holder = [alt.Tooltip('Dato:T', title='Date'), alt.Tooltip('Nye tilfeller', title='New Cases'), alt.Tooltip('Kumulativt antall', title='Cumulative Cases')]

    df = readCSV(county, start, end)
    county = re.search(r'.*(?=\.)', county).group(0)

    chart = alt.Chart(df).mark_bar().encode(
        x=alt.X('yearmonthdate(Dato):T', title="Date (" + start + " - " + end+ ")", axis=alt.Axis(
        format="%d-%m-%Y",
        labelOverlap=False,
        labelAngle=-45,)),
        y= alt.Y('Nye tilfeller', axis=alt.Axis(title='New Cases', titleColor='#4d78a8')),
        tooltip=tooltip_holder
    ).properties(
        height=600,
        width=1200,
        title="New Cases in " + county
    ).interactive(bind_y=False)

    if (display == True):
        chart.show()

    return chart

def plot_cumulative_cases(county=default_county, start=start_date, end=end_date, display=True, display_both=False):
    """
    Plots a line plot of cumulative cases and displays it
    Args:
        county (str): name of the csv file
        start (str): start date in format dd/mm/yyyy
        end (str): end date in format dd/mm/yyyy
        display (bool): if true, displays the plot with altair-viewer
        display_both (bool): if true, displays both the number of cumulative and new cases in a hover box
    Returns:
        chart (altair chart)
    """
    tooltip_holder = [alt.Tooltip('Dato:T', title='Date'), alt.Tooltip('Kumulativt antall', title='Cumulative Cases')]
    if (display_both == True):
         tooltip_holder = [alt.Tooltip('Dato:T', title='Date'), alt.Tooltip('Nye tilfeller', title='New Cases'), alt.Tooltip('Kumulativt antall', title='Cumulative Cases')]

    df = readCSV(county, start, end)
    county = re.search(r'.*(?=\.)', county).group(0)

    chart = alt.Chart(df).mark_line(point=True, stroke='#2E8B57').encode(
        x=alt.X('yearmonthdate(Dato):T', title="Date (" + start + " - " + end + ")", axis=alt.Axis(
        format="%d-%m-%Y",
        labelOverlap=False,
        labelAngle=-45,)),
        y=alt.Y('Kumulativt antall', axis=alt.Axis(title='Cumulative Cases', titleColor='#2E8B57')),
        tooltip=tooltip_holder
    ).properties(
        height=600,
        width=1200,
        title="Cumulative Cases in " + county
    ).interactive(bind_y=False)

    if (display == True):
        chart.show()

    return chart

def plot_norway(csv):
    """
    Plots a interactive plot of Norway to visualize the reported COVID-19 rate per 100000 inhabitants by county in Norway
    *Code from the IN3110 lecture*
    Args:
        csv (str): csv file
    Returns:
        chart (altair chart)
    """
    df = pd.read_csv(csv, sep=';')
    df['Insidens'] = df['Insidens'].map(lambda x: x.replace(',', '.'))
    df['Insidens'] = df['Insidens'].astype(float)

    #Get the topojson of norway counties from random github
    counties = alt.topo_feature("https://raw.githubusercontent.com/deldersveld/topojson/master/countries/norway/norway-new-counties.json", "Fylker")

    nearest = alt.selection(type='single', on="mouseover", fields=["properties.navn"], empty="none")
    #Plot the map
    fig = alt.Chart(counties).mark_geoshape().encode(
        #Enable hover effect
        tooltip=[
            alt.Tooltip("properties.navn:N", title="County"),
            alt.Tooltip("Insidens:Q", title="Cases per 100k")
        ],
        color = alt.Color("Insidens:Q", scale=alt.Scale(scheme="reds"), legend=alt.Legend(title="Cases per 100k")),
        stroke=alt.condition(nearest, alt.value("gray"), alt.value(None)),
        opacity=alt.condition(nearest, alt.value(1), alt.value(0.8)),
        #Lookup number of cases from Pandas table and map to counties
        ).transform_lookup(
            lookup="properties.navn",
            from_=alt.LookupData(df, "Category", ["Insidens"])
        ).properties(
            width=500,
            height=600,
            title="Number of cases per 100k innhabitants in every county",
        ).add_selection(
            nearest
        )

    return fig

def plot_both():
    """
    This is a modified version of task 6.2 (no county, startdate, end date parameters). Plots both cumulative cases and reported cases into a single plot.
    Single plots created by plot_reported_cases() and plot_cumulative_cases() will also be included.
    All three plots are shown in a local browser using Flask after choosing county, startdate and enddate from the dropdown menu.
    In addition, Help page and map page is also created here.
    Args:
        None
    Returns:
        None
    """
    counties = ['Alle fylker', 'Agder', 'Innlandet', 'More og Romsdal', 'Nordland', 'Oslo', 'Rogaland', 'Troms og Finnmark', 'Trondelag', 'Vestfold og Telemark', 'Vestland', 'Viken']

    app = Flask(__name__)

    @app.route('/')
    def dropdown():
        return render_template('plots.html', counties=counties)

    @app.route('/plots', methods = ['POST'])
    def dropdown_result():

        county_choice = request.form.get("county_choice")
        csv_file = counties_dict.get(county_choice)

        start_string = request.form.get("start_date")
        start_datetime = datetime.strptime(start_string, '%Y-%m-%d')

        end_string = request.form.get("end_date")
        end_datetime = datetime.strptime(end_string, '%Y-%m-%d')

        if (end_datetime < start_datetime):
            error = "INVALID USE OF DATE. END DATE MUST BE GREATER THAN START DATE"
            return render_template("plots.html", error=error, counties=counties)

        start = re.sub(r'(\w+)-(\w+)-(\w+)', r'\3/\2/\1', start_string)
        end = re.sub(r'(\w+)-(\w+)-(\w+)', r'\3/\2/\1', end_string)

        #----------------Charts not showing both values in hover text ---------
        bar_plot = plot_reported_cases(csv_file, start, end, False, False)
        line_plot = plot_cumulative_cases(csv_file, start, end, False, False)

        #---------------For layered chart with Dual Axis --------------
        bar_plot_temp = plot_reported_cases(csv_file, start, end, False, True)
        line_plot_temp = plot_cumulative_cases(csv_file, start, end, False, True)

        chart = alt.layer(line_plot_temp, bar_plot_temp).resolve_scale(
            y = 'independent'
        ).properties(
            title="Both Cases in " + county_choice
        )

        all_charts = alt.vconcat(chart, bar_plot, line_plot)
        #------------------------------------------------------------------

        fig = all_charts
        fig.save("chart.html") #Hardcoded, I know

        # tmp = tempfile.NamedTemporaryFile(suffix=".html")
        # fig.save(tmp.name)

        shutil.move("chart.html", "templates/chart.html")

        return render_template("chart.html") #Creates new chart page everytime when opening plot page

    @app.route('/help')
    def help():
        pydoc.writedoc("web_visualization")  #Creates new help page everytime when opening help page
        shutil.move("web_visualization.html", "templates/web_visualization.html")

        return render_template("web_visualization.html")


    @app.route('/map')
    def map():
        fig = plot_norway("interactive map.csv") #Creates new plot everytime when opening  the map page
        fig.save("map.html")
        shutil.move("map.html", "templates/map.html")

        return render_template("map.html")


    if __name__ == "__main__":
        app.run(debug=True)

def interface():
    """
    This is for task 6.1, where users are supposed to choose
    the start date, end date and county in order to create
    plot for reported and cumulative cases. This function
    helps the user through an interface
    Args:
        None
    Returns:
        None
    """

    user_input = 0
    while(user_input not in [1,2]):
        print("Enter one of the following numbers: ")
        print("1 -> Show reported case chart")
        print("2 -> Show cumulative case chart")

        user_input = input("Enter number: ")

        try:
            user_input = int(user_input)
        except ValueError:
            print("***Please choose a number between 1-2***")

    print("\nPlease enter dates between 21-02-2020 - 05-11-2020. Press Enter to skip!")
    start = input("Enter start date in format dd-mm-YYYY: ") #Doesn't handle incorrect dates
    end = input("Enter end date in format dd-mm-YYYY: ") #Doesn't handle incorrect dates

    if not start:
        start = start_date
        print("> Default start date chosen: " + start_date)

    if not end:
        end = end_date
        print("> Default end date chosen: " + end_date)

    print("\nPlease choose one of the following counties. Press Enter to skip!")
    for i in counties_dict:
        print(i)
    print("\nPlease make sure that you write the county as exact as it is written above")
    county = input("Enter county: ")

    if not county:
        print("> Default county = Alle fylker chosen")
        county = "Alle fylker"
    elif (county not in counties_dict):
        print("***County invalid, default county chosen***\n")
        print("> Default county = Alle fylker chosen")
        county = "Alle fylker"

    if user_input == 1:
        plot_reported_cases(counties_dict.get(county), start, end)

    elif user_input == 2:
        plot_cumulative_cases(counties_dict.get(county), start, end)





#----------------------- MAIN -------------------------------------

print("-----------------------ASSIGNMENT 6-----------------------")

#Please uncomment function interface() in order to try task 6.1

#Run function interface() to run either plot_reported_cases() or plot_cumulative_cases() individually

#interface()

plot_both()
