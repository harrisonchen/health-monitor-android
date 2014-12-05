module Api
	module V1
		class MonitorController < ApplicationController
			def status
				temperatureIds = Temperature.all.order('id DESC').limit(5).pluck(:id)
				averageTemperature = Temperature.where(id: temperatureIds).average(:fahrenheit).to_i
				heartBeatIds = Heartbeat.all.order('id DESC').limit(5).pluck(:id)
				averageHeartBeat = Heartbeat.where(id: heartBeatIds).average(:beats_per_minute).to_i

				if averageHeartBeat == 0
					render json: {
						average_temperature: averageTemperature,
						average_heartbeat: averageHeartBeat,
						message: "You are dead"
					}
				elsif averageHeartBeat < 10
					render json: {
						average_temperature: averageTemperature,
						average_heartbeat: averageHeartBeat,
						message: "You better be in the ER right now"
					}
				elsif averageHeartBeat < 30
					render json: {
						average_temperature: averageTemperature,
						average_heartbeat: averageHeartBeat,
						message: "You may be getting a heart attack right now"
					}
				elsif averageTemperature >= 90 && averageHeartBeat <= 100
					render json: {
						average_temperature: averageTemperature,
						average_heartbeat: averageHeartBeat,
						message: "You are potentially sick"
					}
				elsif averageTemperature >= 90 && averageHeartBeat > 100
					render json: {
						average_temperature: averageTemperature,
						average_heartbeat: averageHeartBeat,
						message: "You are either really sick or exercising"
					}
				elsif averageTemperature < 90 && averageHeartBeat > 100
					render json: {
						average_temperature: averageTemperature,
						average_heartbeat: averageHeartBeat,
						message: "You are either really excited or having an anxiety attack"
					}
				else
					render json: {
						average_temperature: averageTemperature,
						average_heartbeat: averageHeartBeat,
						message: "You are in good condition"
					}
				end
			end
		end
	end
end
