class CreateTemperatures < ActiveRecord::Migration
  def change
    create_table :temperatures do |t|
      t.decimal :fahrenheit, default: 0.0
      t.decimal :celsius, default: 0.0
      t.decimal :kelvin, default: 0.0

      t.timestamps
    end
  end
end
